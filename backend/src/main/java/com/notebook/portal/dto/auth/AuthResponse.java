package com.notebook.portal.dto.auth;

import com.notebook.portal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private Long userId;
    private String name;
    private Role role;
    private String token;
}
