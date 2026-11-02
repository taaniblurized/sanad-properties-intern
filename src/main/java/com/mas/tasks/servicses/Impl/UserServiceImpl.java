package com.mas.tasks.servicses.Impl;

import com.mas.tasks.dtos.RegisterRequest;
import com.mas.tasks.dtos.UpdateUserRequest;
import com.mas.tasks.dtos.UserResponse;
import com.mas.tasks.entities.User;
import com.mas.tasks.repositories.UserRepository;
import com.mas.tasks.servicses.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = findUserById(id);

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = findUserById(id);

        if (userRepository.existsByEmail(request.getEmail())
                && !user.getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists"
            );
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Update password only if provided
        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = findUserById(id);

        userRepository.delete(user);
    }

    private User findUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found"
                        )
                );
    }

    private UserResponse mapToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}