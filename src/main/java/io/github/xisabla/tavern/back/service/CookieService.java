package io.github.xisabla.tavern.back.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Utility service to handle cookies.
 */
@Service
public class CookieService {
    @Value("${cookie.auth.name}")
    private String authCookieName;

    @Value("${cookie.auth.max-age}")
    private int authCookieMaxAge;

    @Value("${cookie.auth.max-age.remember}")
    private int authCookieMaxAgeRemember;

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    /**
     * Create a HttpOnly cookie for authentication.
     *
     * @param name   Name of the cookie to create
     * @param value  Value of the cookie
     * @param maxAge Expiration time of the cookie
     * @return The created cookie
     * @link https://owasp.org/www-community/HttpOnly
     */
    public Cookie createHttpOnlyCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    /**
     * Create an authentication cookie.
     *
     * @param token    Token to store in the cookie
     * @param remember Whether to remember the user
     * @return The created cookie
     */
    public Cookie createAuthCookie(String token, boolean remember) {
        int maxAge = remember ? authCookieMaxAgeRemember : authCookieMaxAge;

        return createHttpOnlyCookie(authCookieName, token, maxAge);
    }

    /**
     * Get a cookie from the request.
     *
     * @param request Request to get the cookie from
     * @param name    Name of the cookie to get
     * @return The cookie if it exists, empty otherwise
     */
    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
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
     * Get the authentication cookie from the request.
     *
     * @param request Request to get the cookie from
     * @return The authentication cookie if it exists, empty otherwise
     */
    public Optional<Cookie> getAuthCookie(HttpServletRequest request) {
        return getCookie(request, authCookieName);
    }
}
