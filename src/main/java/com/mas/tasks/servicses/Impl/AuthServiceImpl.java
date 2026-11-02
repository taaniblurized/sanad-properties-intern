package com.mas.tasks.servicses.Impl;



import com.mas.tasks.dtos.AuthResponse;
import com.mas.tasks.dtos.LoginRequest;
import com.mas.tasks.dtos.RegisterRequest;
import com.mas.tasks.entities.Role;
import com.mas.tasks.entities.User;
import com.mas.tasks.repositories.UserRepository;
import com.mas.tasks.security.JwtService;
import com.mas.tasks.servicses.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign default role
        user.setRole(Role.USER);

        // Save user
        userRepository.save(user);

        // Load UserDetails
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        // Generate JWT
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Get user from database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        // Load UserDetails
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        // Generate JWT
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
