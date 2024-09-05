package io.github.xisabla.back.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.xisabla.back.service.CookieService;
import io.github.xisabla.back.service.JwtService;
import io.github.xisabla.back.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filter to authenticate users using JSON Web Tokens.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CookieService cookieService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Filters the request to authenticate users using JSON Web Tokens.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        final Optional<Cookie> authTokenCookie = cookieService.getAuthTokenCookie(request);

        if (authTokenCookie.isEmpty()) {
            filterChain.doFilter(request, response);

            return;
        }

        try {
            final String token = authTokenCookie.get().getValue();
            final String username = jwtService.extractUsername(token);

            if (username == null || !jwtService.validateToken(token)) {
                filterChain.doFilter(request, response);

                return;
            }

            final UserDetails user = userDetailsService.loadUserByUsername(username);
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

}
