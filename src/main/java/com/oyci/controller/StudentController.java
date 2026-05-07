package com.oyci.controller;

import com.oyci.dto.StudentDtos;
import com.oyci.model.Student;
import com.oyci.repository.StudentRepository;
import com.oyci.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<StudentDtos.StudentResponse> getMyProfile(HttpServletRequest request) {
        Long studentId = extractUserId(request);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return ResponseEntity.ok(toResponse(student));
    }

    @PutMapping("/me/skills")
    public ResponseEntity<StudentDtos.StudentResponse> updateSkills(
            @RequestBody StudentDtos.UpdateSkillsRequest req,
            HttpServletRequest request) {
        Long studentId = extractUserId(request);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setSkills(req.getSkills() == null ? new ArrayList<>() : new ArrayList<>(req.getSkills()));
        studentRepository.save(student);
        return ResponseEntity.ok(toResponse(student));
    }

    @PostMapping("/me/dismiss-banner")
    public ResponseEntity<Void> dismissBanner(HttpServletRequest request) {
        Long studentId = extractUserId(request);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setShowCertificateBanner(false);
        studentRepository.save(student);
        return ResponseEntity.ok().build();
    }

    private Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return jwtUtil.extractUserId(header.substring(7));
    }

    private StudentDtos.StudentResponse toResponse(Student student) {
        return new StudentDtos.StudentResponse(
                student.getId(), student.getUsername(), student.getName(), student.getEmail(),
                student.isSpecialNeeds(), student.getSkills(),
                student.getCompletedEventIds() == null ? List.of() : student.getCompletedEventIds(),
                student.isShowCertificateBanner()
        );
    }
}
