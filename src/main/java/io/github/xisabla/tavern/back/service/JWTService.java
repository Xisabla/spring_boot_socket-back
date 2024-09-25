package io.github.xisabla.tavern.back.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * Service to manage JSON Web Tokens.
 */
@Service
public class JWTService {
    /**
     * Number of milliseconds in a second.
     */
    private static final int MILLIS_IN_SECOND = 1000;

    /**
     * Secret key to sign the tokens.
     * <p>
     * Must be defined in the application properties and must be kept secret. Must be strong enough to be accepted by
     * the algorithm used to sign the tokens.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Duration of the token validity in seconds.
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Create a token for a user, with the given additional claims.
     *
     * @param claims      Additional claims to add to the token.
     * @param userDetails User to create the token for.
     *
     * @return A JWT token claiming the user.
     *
     * @throws InvalidKeyException If the secret key is invalid.
     */
    private String buildToken(final Map<String, Object> claims, final UserDetails userDetails)
        throws InvalidKeyException {
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(now.getTime() + expiration * MILLIS_IN_SECOND);

        return Jwts.builder()
                   .claims(claims)
                   .subject(userDetails.getUsername())
                   .issuedAt(now)
                   .expiration(expirationDate)
                   .signWith(getSignInKey())
                   .compact();
    }

    /**
     * Create a token to identify a user.
     *
     * @param userDetails User to create the token for.
     *
     * @return The JWT token identifying the user.
     *
     * @throws InvalidKeyException If the secret key is invalid.
     */
    public String generateToken(final UserDetails userDetails) throws InvalidKeyException {
        return buildToken(Map.of(), userDetails);
    }

    /**
     * Extract the claims from a token.
     *
     * @param token Token to extract the claims from.
     *
     * @return The claims extracted from the token.
     *
     * @throws JwtException             If the token is malformed or if the secret key is invalid.
     * @throws IllegalArgumentException If the token is invalid (null).
     */
    private Claims extractClaims(final String token) throws JwtException, IllegalArgumentException {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Check if a token is expired.
     *
     * @param token Token to check.
     *
     * @return True if the token is expired, false otherwise.
     *
     * @throws JwtException             If the token is malformed or if the secret key is invalid.
     * @throws IllegalArgumentException If the token is invalid (null).
     */
    private boolean isTokenExpired(final String token) throws JwtException, IllegalArgumentException {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Check if a token is readable and not expired.
     *
     * @param token Token to validate.
     *
     * @return True if the token is readable and not expired, false otherwise.
     */
    public boolean validateToken(final String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extract the username from a token.
     *
     * @param token Token to extract the username from.
     *
     * @return The username extracted from the token.
     *
     * @throws JwtException             If the token is malformed or if the secret key is invalid.
     * @throws IllegalArgumentException If the token is invalid (null).
     */
    public String extractUsername(final String token) throws JwtException, IllegalArgumentException {
        return extractClaims(token).getSubject();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = secret.getBytes();

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
