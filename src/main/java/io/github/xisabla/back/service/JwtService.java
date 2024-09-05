package io.github.xisabla.back.service;

import java.util.Map;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

/**
 * Service for JSON Web Token generation and validation.
 */
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails user) throws InvalidKeyException {
        return buildToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> claims, UserDetails user)
            throws InvalidKeyException {
        return buildToken(claims, user);
    }

    public String extractUsername(String token) throws InvalidKeyException, JwtException, IllegalArgumentException {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (RuntimeException e) {
            return false;
        }
    }

    private String buildToken(Map<String, Object> claims, UserDetails user) throws InvalidKeyException {
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private Claims extractClaims(String token) throws InvalidKeyException, JwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) throws JwtException, IllegalArgumentException, NullPointerException {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getSignInKey() throws WeakKeyException {
        byte[] keyBytes = secret.getBytes();

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
