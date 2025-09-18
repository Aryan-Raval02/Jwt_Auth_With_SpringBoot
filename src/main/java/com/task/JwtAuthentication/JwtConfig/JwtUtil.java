package com.task.JwtAuthentication.JwtConfig;

import com.task.JwtAuthentication.Dao.UserDaoRepository;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Exception.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil
{
    private final String SECRET = "my-super-secret-key-that-is-long-enough-123456789!@#";
    private final long ACCESS_EXPIRATION = 1000*60*5; // 5 Minutes
    private final long REFRESH_EXPIRATION = 1000*60*60; // 1 hour
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Autowired private UserDaoRepository userDaoRepository;

    public String generateAccessToken(String uuid, UserDetails userDetails)
    {
        return Jwts.builder()
                .subject(uuid)
                .claim("role",userDetails.getAuthorities().iterator().next().getAuthority()) // Put Role
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String uuid, UserDetails userDetails)
    {
        return Jwts.builder()
                .subject(uuid)
                .claim("role","refresh") // Put Role
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String extractUuid(String token)
    {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(UserDetails userDetails, String token)
    {
        // TODO - check if username is same as username in userDetails
        // TODO - Check if token is not expired
        String uuid = extractUuid(token);
        String check = userDetails.getUsername();

        return uuid.equals(check) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token)
    {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String extractRole(String token)
    {
        return (String) extractClaims(token).get("role");
    }
}
