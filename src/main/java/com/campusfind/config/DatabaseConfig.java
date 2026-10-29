package com.campusfind.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Production Database Configuration.
 * Automatically adapts Render, Supabase, Neon, or standard PostgreSQL DATABASE_URL
 * into valid JDBC DataSource properties.
 */
@Configuration
@Profile("prod")
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${spring.datasource.username:postgres}")
    private String defaultUsername;

    @Value("${spring.datasource.password:postgres}")
    private String defaultPassword;

    @Bean
    public DataSource dataSource() {
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url("jdbc:postgresql://localhost:5432/campusfind")
                    .username(defaultUsername)
                    .password(defaultPassword)
                    .build();
        }

        // If already formatted with jdbc: prefix
        if (databaseUrl.startsWith("jdbc:")) {
            return DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url(databaseUrl)
                    .username(defaultUsername)
                    .password(defaultPassword)
                    .build();
        }

        try {
            // Parse Render's postgres://user:password@hostname:port/database URI
            URI uri = new URI(databaseUrl);
            String host = uri.getHost();
            int port = (uri.getPort() > 0) ? uri.getPort() : 5432;
            String path = uri.getPath(); // e.g. /campusfind
            String userInfo = uri.getUserInfo();

            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
            String username = defaultUsername;
            String password = defaultPassword;

            if (userInfo != null && userInfo.contains(":")) {
                String[] parts = userInfo.split(":", 2);
                username = parts[0];
                password = parts[1];
            }

            return DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .build();

        } catch (URISyntaxException e) {
            throw new IllegalStateException("Failed to parse production DATABASE_URL URI: " + databaseUrl, e);
        }
    }
}
