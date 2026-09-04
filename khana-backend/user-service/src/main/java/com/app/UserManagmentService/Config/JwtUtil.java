package com.app.UserManagmentService.Config;
import com.app.UserManagmentService.Entity.Refresh;
import com.app.UserManagmentService.Repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private  String SECRET ;

    @Value("${jwt.expiration}")
    private  String TOKEN_EXPIRATION_TIME ;

    @Value("${jwt.refresh-expiration}")
    private  String REFRESH_TOKEN_EXPIRATION_TIME ;



    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

//    private final String SECRET= token_secret;
//    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//    private final long TOKEN_EXPIRATION_TIME = token_expiration; // 1 hour in milliseconds
//    private final long REFRESH_TOKEN_EXPIRATION_TIME = refresh_expiration;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public boolean validateRefreshToken(String token) {

        // 1. Find token in DB
        Refresh refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));

        // 2. Check if revoked
        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (refreshToken.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return true;

    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String extractedname = extractUserName(token);
        System.out.println("Extracted username:" + extractedname);
        System.out.println(" username:" + userDetails.getUsername());
        System.out.println("istoken expired::" + isTokenExpired(token));

        return extractUserName(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractClaims(token).getExpiration().before(new Date());
    }
}