package com.campusfind.config;

import com.campusfind.entity.User;
import com.campusfind.enums.Role;
import com.campusfind.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Production Administrator Initializer.
 * Creates an initial administrator account ONLY if CAMPUSFIND_ADMIN_PASSWORD is
 * provided in production environment variables and no admin account exists yet.
 */
@Component
@Profile("prod")
public class ProductionAdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductionAdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${campusfind.admin.name:System Administrator}")
    private String adminName;

    @Value("${campusfind.admin.email:admin@campusfind.edu}")
    private String adminEmail;

    @Value("${campusfind.admin.password:}")
    private String adminPassword;

    public ProductionAdminInitializer(UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (adminPassword == null || adminPassword.isBlank()) {
            log.info("Production mode: No CAMPUSFIND_ADMIN_PASSWORD environment variable supplied. Skipping admin creation.");
            return;
        }

        String email = adminEmail.trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            log.info("Production mode: Administrator account '{}' already exists.", email);
            return;
        }

        User admin = new User(
                adminName.trim(),
                email,
                passwordEncoder.encode(adminPassword.trim()),
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);
        log.info("Production mode: Initial administrator account '{}' created successfully from environment variables.", email);
    }
}
