package io.github.xisabla.back.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for cookie operations.
 */
@Service
public class CookieService {
    @Value("${cookie.token.name}")
    private String tokenCookieName;

    @Value("${cookie.token.maxAge}")
    private int tokenCookieMaxAge;

    @Value("${cookie.token.maxAge.remember}")
    private int tokenCookieMaxAgeRemember;

    @Value("${cookie.token.secure}")
    private boolean tokenCookieSecure;

    /**
     * Create a new HTTP-only cookie
     *
     * @param name   Name of the cookie to create
     * @param value  Value of the cookie
     * @param maxAge Maximum age of the cookie
     * @return The created cookie
     */
    public Cookie createHttpOnlyCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(tokenCookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    /**
     * Get a cookie from the request by its name
     *
     * @param request The request to get the cookie from
     * @param name    The name of the cookie to get
     * @return An optional containing the cookie if it exists, empty otherwise
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }

    /**
     * Create a new authentication token cookie
     *
     * @param token The token to store in the cookie
     * @return The created cookie
     */
    public Cookie createAuthTokenCookie(String token) {
        return createHttpOnlyCookie(tokenCookieName, token, tokenCookieMaxAge);
    }

    /**
     * Get the auth token cookie from the request
     *
     * @param request The request to get the cookie from
     * @return An optional containing the cookie if it exists, empty otherwise
     */
    public Optional<Cookie> getAuthTokenCookie(HttpServletRequest request) {
        return getCookie(request, tokenCookieName);
    }
}
