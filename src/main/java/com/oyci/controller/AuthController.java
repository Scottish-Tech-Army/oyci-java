package com.oyci.controller;

import com.oyci.dto.AuthDtos;
import com.oyci.dto.StudentDtos;
import com.oyci.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/student")
    public ResponseEntity<AuthDtos.AuthResponse> registerStudent(
            @Valid @RequestBody StudentDtos.RegisterStudentRequest request) {
        return ResponseEntity.ok(authService.registerStudent(request));
    }
}
