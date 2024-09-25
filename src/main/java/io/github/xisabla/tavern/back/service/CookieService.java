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
    /**
     * Name of the authentication cookie.
     */
    @Value("${cookie.auth.name}")
    private String authCookieName;

    /**
     * Expiration time of the auth cookie.
     */
    @Value("${cookie.auth.max-age}")
    private int authCookieMaxAge;

    /**
     * Expiration time of the auth cookie when the user wants to be remembered.
     */
    @Value("${cookie.auth.max-age.remember}")
    private int authCookieMaxAgeRemember;

    /**
     * Whether the auth cookie should be secure.
     * <p>
     * Secure cookies are only sent over HTTPS connections. This setting depends on the environment, and should be set
     * to true in production.
     */
    @Value("${cookie.secure}")
    private boolean cookieSecure;

    /**
     * Create a HttpOnly cookie for authentication.
     *
     * @param name   Name of the cookie to create
     * @param value  Value of the cookie
     * @param maxAge Expiration time of the cookie
     *
     * @return The created cookie, to be added to the response
     *
     * @see <a href="https://owasp.org/www-community/HttpOnly">https://owasp.org/www-community/HttpOnly</a>
     */
    public Cookie createHttpOnlyCookie(final String name, final String value, final int maxAge) {
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
     *
     * @return The created cookie, to be added to the response
     */
    public Cookie createAuthCookie(final String token, final boolean remember) {
        int maxAge = remember ? authCookieMaxAgeRemember : authCookieMaxAge;

        return createHttpOnlyCookie(authCookieName, token, maxAge);
    }

    /**
     * Create a cookie to logout the user. This cookie will have an expiration time of 0 and an empty value.
     *
     * @return The created cookie, to be added to the response
     */
    public Cookie createLogoutCookie() {
        return createHttpOnlyCookie(authCookieName, "", 0);
    }

    /**
     * Get a cookie from the request.
     *
     * @param request Request to get the cookie from
     * @param name    Name of the cookie to get
     *
     * @return The cookie if it exists, empty otherwise
     */
    public Optional<Cookie> getCookie(final HttpServletRequest request, final String name) {
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
     *
     * @return The authentication cookie if it exists, empty otherwise
     */
    public Optional<Cookie> getAuthCookie(final HttpServletRequest request) {
        return getCookie(request, authCookieName);
    }
}
