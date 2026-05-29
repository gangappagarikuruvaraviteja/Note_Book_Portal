package com.notebook.portal.service;

import com.notebook.portal.dto.auth.AuthRequest;
import com.notebook.portal.dto.auth.AuthResponse;
import com.notebook.portal.dto.auth.RegisterRequest;
import com.notebook.portal.entity.User;
import com.notebook.portal.exception.BadRequestException;
import com.notebook.portal.repository.UserRepository;
import com.notebook.portal.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getEmail());
        return new AuthResponse(saved.getId(), saved.getName(), saved.getRole(), token);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(user.getId(), user.getName(), user.getRole(), token);
    }
}
