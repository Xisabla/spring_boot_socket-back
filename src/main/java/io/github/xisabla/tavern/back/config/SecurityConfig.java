package io.github.xisabla.tavern.back.config;

import io.github.xisabla.tavern.back.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * All configuration related to security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * JWT authentication filter.
     */
    private final JWTAuthFilter jwtAuthFilter;
    /**
     * User details service for authentication.
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Allowed origin for CORS.
     */
    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    /**
     * Security filter chain configuration.
     *
     * @param http The http security configuration.
     *
     * @return The security filter chain for the application.
     *
     * @throws Exception If an error occurs during the configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(requests -> requests
                // Info
                .requestMatchers(HttpMethod.GET, "/info/healthcheck")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/info/version")
                .permitAll()
                // Auth
                .requestMatchers(HttpMethod.POST, "/auth/register")
                .permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "/auth/login")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/logout")
                .authenticated()
                .requestMatchers(HttpMethod.GET, "/auth/validate")
                .authenticated()
                .requestMatchers(HttpMethod.GET, "/auth/refresh")
                .authenticated()
                // Any
                .anyRequest()
                .authenticated());

        http.authenticationProvider(authProvider());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Authentication provider using the user details service and password encoder.
     *
     * @return The application authentication provider.
     */
    @Bean
    AuthenticationProvider authProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * Password encoder using BCrypt.
     *
     * @return The password encoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Allow CORS from the specified origin.
     *
     * @return The CORS configuration source.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern(allowedOrigin);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
