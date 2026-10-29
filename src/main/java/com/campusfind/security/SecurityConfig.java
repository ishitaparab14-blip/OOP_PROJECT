package com.campusfind.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabled for simplicity with REST JSON clients
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        // In dev mode with H2 enabled, allow frames for H2 console
        if (h2ConsoleEnabled) {
            http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        }

        http.authorizeHttpRequests(auth -> {
            // Static web pages and assets
            auth.requestMatchers(
                "/",
                "/index.html",
                "/browse.html",
                "/item-details.html",
                "/report.html",
                "/dashboard.html",
                "/admin.html",
                "/login.html",
                "/register.html",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico"
            ).permitAll();

            // Public REST endpoints
            auth.requestMatchers("/api/auth/**").permitAll();
            auth.requestMatchers("/api/health").permitAll();
            auth.requestMatchers("/api/stats").permitAll();
            auth.requestMatchers(HttpMethod.GET, "/api/items/**").permitAll();

            // H2 Database Console - ONLY accessible in local development when explicitly enabled
            if (h2ConsoleEnabled) {
                auth.requestMatchers("/h2-console/**").permitAll();
            }

            // Admin-only endpoints
            auth.requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN");

            // All other API endpoints require authenticated student or admin
            auth.requestMatchers("/api/**").authenticated();
            auth.anyRequest().permitAll();
        });

        http.exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
                if (request.getRequestURI().startsWith("/api/")) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Please log in to continue.\"}");
                } else {
                    response.sendRedirect("/login.html");
                }
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                if (request.getRequestURI().startsWith("/api/")) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Access denied. Administrator privileges required.\"}");
                } else {
                    response.sendRedirect("/index.html");
                }
            })
        );

        http.logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\":true,\"message\":\"Logged out successfully.\"}");
            })
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
        );

        return http.build();
    }
}
