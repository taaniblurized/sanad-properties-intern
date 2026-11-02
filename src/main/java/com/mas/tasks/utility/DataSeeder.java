package com.mas.tasks.utility;

import com.mas.tasks.entities.User;
import com.mas.tasks.entities.Role;
import com.mas.tasks.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String adminEmail = "admin@gmail.com";

        if (!userRepository.existsByEmail(adminEmail)) {

            User admin = new User();

            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("Admin account created successfully!");
        }
    }
}