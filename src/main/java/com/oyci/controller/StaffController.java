package com.oyci.controller;

import com.oyci.dto.StaffDtos;
import com.oyci.security.JwtUtil;
import com.oyci.service.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<StaffDtos.StaffResponse> getMyProfile(HttpServletRequest request) {
        Long staffId = extractUserId(request);
        return ResponseEntity.ok(staffService.getStaffById(staffId));
    }

    @PutMapping("/me")
    public ResponseEntity<StaffDtos.StaffResponse> updateMyProfile(
            @RequestBody StaffDtos.StaffSelfUpdateRequest req,
            HttpServletRequest request) {
        Long staffId = extractUserId(request);
        return ResponseEntity.ok(staffService.selfUpdate(staffId, req));
    }

    @PostMapping("/me/holidays")
    public ResponseEntity<StaffDtos.StaffResponse> addHoliday(
            @RequestBody StaffDtos.HolidayRequest req,
            HttpServletRequest request) {
        Long staffId = extractUserId(request);
        return ResponseEntity.ok(staffService.addHoliday(staffId, req.getHolidayDate()));
    }

    @DeleteMapping("/me/holidays/{date}")
    public ResponseEntity<StaffDtos.StaffResponse> removeHoliday(
            @PathVariable String date,
            HttpServletRequest request) {
        Long staffId = extractUserId(request);
        return ResponseEntity.ok(staffService.removeHoliday(staffId, date));
    }

    @PostMapping("/me/withdraw")
    public ResponseEntity<Void> withdrawFromEvent(
            @RequestBody StaffDtos.WithdrawRequest req,
            HttpServletRequest request) {
        Long staffId = extractUserId(request);
        staffService.withdrawFromEvent(staffId, req.getEventId());
        return ResponseEntity.ok().build();
    }

    private Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return jwtUtil.extractUserId(header.substring(7));
    }
}
