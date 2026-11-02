package com.mas.tasks.dtos;

import com.mas.tasks.entities.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NotBlank
@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}