package com.task.JwtAuthentication.JwtConfig;

import com.task.JwtAuthentication.Dao.UserDaoRepository;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Security.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDaoRepository userDaoRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = extractToken(request, "accessToken");
        String refreshToken = extractToken(request, "refreshToken");

        try {
            if (accessToken != null && !jwtUtil.isTokenExpired(accessToken)) {
                setAuthFromAccessToken(accessToken, request);
            } else if (refreshToken != null && !jwtUtil.isTokenExpired(refreshToken)) {
                // Generate new access token using refresh token
                String newAccessToken = refreshAccessToken(refreshToken, response, request);
                if (newAccessToken != null) {
                    setAuthFromAccessToken(newAccessToken, request);
                }
            }
        } catch (Exception e) {
            logger.warn("JWT processing failed: {}");
        }

        filterChain.doFilter(request, response);
    }

    private String refreshAccessToken(String refreshToken, HttpServletResponse response, HttpServletRequest request) {
        String uuid = jwtUtil.extractUuid(refreshToken);
        if (uuid == null) return null;

        User user = userDaoRepository.findByUuid(uuid);
        if (user == null) return null;

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        if (!jwtUtil.validateToken(userDetails, refreshToken)) return null;

        String newAccessToken = jwtUtil.generateAccessToken(uuid, userDetails);

        ResponseCookie cookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(5 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return newAccessToken;
    }

    private void setAuthFromAccessToken(String token, HttpServletRequest request) {
        String uuid = jwtUtil.extractUuid(token);
        String role = jwtUtil.extractRole(token);

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role)
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(uuid, null, authorities);

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String extractToken(HttpServletRequest request, String cookieName) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && "accessToken".equals(cookieName)) {
            return authHeader.substring(7);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) return cookie.getValue();
            }
        }

        return null;
    }
}

