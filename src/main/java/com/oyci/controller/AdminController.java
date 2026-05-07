package com.oyci.controller;

import com.oyci.dto.StaffDtos;
import com.oyci.dto.StudentDtos;
import com.oyci.model.Student;
import com.oyci.repository.StudentRepository;
import com.oyci.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final StaffService staffService;
    private final StudentRepository studentRepository;

    // --- Staff management ---

    @PostMapping("/staff")
    public ResponseEntity<StaffDtos.StaffResponse> createStaff(
            @Valid @RequestBody StaffDtos.CreateStaffRequest request) {
        return ResponseEntity.ok(staffService.createStaff(request));
    }

    @GetMapping("/staff")
    public ResponseEntity<List<StaffDtos.StaffResponse>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/staff/{id}")
    public ResponseEntity<StaffDtos.StaffResponse> getStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<StaffDtos.StaffResponse> updateStaff(
            @PathVariable Long id,
            @RequestBody StaffDtos.UpdateStaffRequest request) {
        return ResponseEntity.ok(staffService.updateStaff(id, request));
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/staff/by-skills")
    public ResponseEntity<List<StaffDtos.StaffResponse>> getStaffBySkills(
            @RequestParam List<String> skills) {
        return ResponseEntity.ok(staffService.getStaffBySkills(skills));
    }

    // --- Student management ---

    @GetMapping("/students")
    public ResponseEntity<List<StudentDtos.StudentResponse>> getAllStudents() {
        List<StudentDtos.StudentResponse> students = studentRepository.findAll()
                .stream()
                .map(s -> new StudentDtos.StudentResponse(
                        s.getId(), s.getUsername(), s.getName(), s.getEmail(),
                        s.isSpecialNeeds(), s.getSkills(),
                        s.getCompletedEventIds() == null ? List.of() : s.getCompletedEventIds(),
                        s.isShowCertificateBanner()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(students);
    }
}
