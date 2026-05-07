package com.oyci.service;

import com.oyci.dto.AuthDtos;
import com.oyci.dto.StudentDtos;
import com.oyci.model.*;
import com.oyci.repository.UserRepository;
import com.oyci.repository.StudentRepository;
import com.oyci.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails, user.getRole().name(), user.getId());
        return new AuthDtos.AuthResponse(token, user.getUsername(), user.getRole(), user.getId());
    }

    public AuthDtos.AuthResponse registerStudent(StudentDtos.RegisterStudentRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken: " + request.getUsername());
        }
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(Role.STUDENT);
        student.setSkills(request.getSkills() == null ? new ArrayList<>() : new ArrayList<>(request.getSkills()));
        student.setSpecialNeeds(request.isSpecialNeeds());
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        studentRepository.save(student);

        UserDetails userDetails = userDetailsService.loadUserByUsername(student.getUsername());
        String token = jwtUtil.generateToken(userDetails, student.getRole().name(), student.getId());
        return new AuthDtos.AuthResponse(token, student.getUsername(), student.getRole(), student.getId());
    }
}
