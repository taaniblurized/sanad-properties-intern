package com.mas.tasks.servicses;

import com.mas.tasks.dtos.RegisterRequest;
import com.mas.tasks.dtos.UpdateUserRequest;
import com.mas.tasks.dtos.UserResponse;

import java.util.List;

public interface UserService {


    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
}