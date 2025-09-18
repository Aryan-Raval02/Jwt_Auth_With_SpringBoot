package com.task.JwtAuthentication.Controller;

import com.task.JwtAuthentication.Dao.UserDaoRepository;
import com.task.JwtAuthentication.Dto.AuthenticateRequest;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Exception.UserNotFoundException;
import com.task.JwtAuthentication.JwtConfig.JwtUtil;
import com.task.JwtAuthentication.Security.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController
{
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserDaoRepository userDaoRepository;
    @Autowired private JwtUtil jwtUtil;

    @Autowired private CustomUserDetailsService customUserDetailsService;

    // Authenticate User
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticateRequest request, HttpServletResponse response)
    {
        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());

            User user = userDaoRepository.findByUsername(request.getUsername());
            if(user == null) throw new UserNotFoundException("Seller Not Found");

            // create JWT Token
            String accessToken = jwtUtil.generateAccessToken(user.getUuid(), userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(user.getUuid(), userDetails);

            // Access Token (5 Min)
            ResponseCookie cookie = ResponseCookie.from("accessToken",accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(5*60)
                    .build();

            // Refresh Token (1 hour)
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken",refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(60*60)
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());
            response.addHeader("Set-Cookie",responseCookie.toString());

            return ResponseEntity.ok("Login Successful, JWT set in cookie");
        }
        catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username & Password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response)
    {
//        // Clear access token
//        ResponseCookie clearAccess = ResponseCookie.from("accessToken", "")
//                .httpOnly(true)
//                .secure(false) // false for HTTP, true for HTTPS
//                .path("/")
//                .maxAge(0)
//                .sameSite("Strict")
//                .build();
//
//        // Clear refresh token
//        ResponseCookie clearRefresh = ResponseCookie.from("refreshToken", "")
//                .httpOnly(true)
//                .secure(false) // false for HTTP, true for HTTPS
//                .path("/")
//                .maxAge(0)
//                .sameSite("Strict")
//                .build();
//
//        response.addHeader("Set-Cookie", clearAccess.toString());
//        response.addHeader("Set-Cookie", clearRefresh.toString());
//
//        // Optional: clear Authorization header
//        response.setHeader("Authorization", "");
//
//        // Clear SecurityContext
//        SecurityContextHolder.clearContext();
//
//        return ResponseEntity.ok("Logout successful. All tokens cleared.");

        // Clear all cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookie.setValue("");          // empty value
                cookie.setPath("/");          // path must match the original cookie
                cookie.setMaxAge(0);          // expire immediately
                cookie.setHttpOnly(true);     // same as original
                cookie.setSecure(false);      // false for dev, true for HTTPS
                response.addCookie(cookie);   // overwrite cookie in response
            }
        }

        // Clear Authorization header
        response.setHeader("Authorization", "");

        // Clear Spring Security context
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logout successful. Cookies and headers cleared.");
    }
}
