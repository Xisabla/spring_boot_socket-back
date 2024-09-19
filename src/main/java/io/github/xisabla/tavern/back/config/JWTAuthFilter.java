package io.github.xisabla.tavern.back.config;

import io.github.xisabla.tavern.back.service.CookieService;
import io.github.xisabla.tavern.back.service.JWTService;
import io.github.xisabla.tavern.back.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Filter to authenticate requests with JSON Web Tokens.
 */
@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final CookieService cookieService;
    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Check if the request has a valid JWT and authenticate the user.
     *
     * @param request     Request to be filtered
     * @param response    Response linked to the request
     * @param filterChain Filter chain to continue the request
     * @throws ServletException If an error occurs during the filter
     * @throws IOException      If an error occurs during the filter
     */
    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final Optional<Cookie> authCookie = cookieService.getAuthCookie(request);

        if (authCookie.isEmpty()) {
            filterChain.doFilter(request, response);

            return;
        }

        try {
            final String token = authCookie.get().getValue();
            final UserDetails user = getUser(token).orElseThrow();

            setAuthenticationToken(user, request);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Retrieves the user from the JWT token.
     *
     * @param token Token extracted from the request
     * @return User details if the token is valid, empty otherwise
     */
    private Optional<UserDetails> getUser(String token) {
        final String username = jwtService.extractUsername(token);

        if (username == null || !jwtService.validateToken(token)) {
            return Optional.empty();
        }

        return Optional.of(userDetailsService.loadUserByUsername(username));
    }

    /**
     * Set the authentication token in the security context.
     *
     * @param user    User to be authenticated
     * @param request Request to be authenticated
     */
    private void setAuthenticationToken(final UserDetails user, @NonNull final HttpServletRequest request) {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
