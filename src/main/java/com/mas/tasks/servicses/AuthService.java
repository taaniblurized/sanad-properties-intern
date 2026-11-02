package com.mas.tasks.servicses;


import com.mas.tasks.dtos.AuthResponse;
import com.mas.tasks.dtos.LoginRequest;
import com.mas.tasks.dtos.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
