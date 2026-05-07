package com.oyci.controller;

import com.oyci.dto.EventDtos;
import com.oyci.dto.StaffDtos;
import com.oyci.model.Student;
import com.oyci.repository.StudentRepository;
import com.oyci.security.JwtUtil;
import com.oyci.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;

    // ── ADMIN endpoints ────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDtos.EventResponse> createEvent(
            @Valid @RequestBody EventDtos.CreateEventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<EventDtos.EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','STUDENT')")
    public ResponseEntity<EventDtos.EventResponse> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDtos.EventResponse> updateEvent(
            @PathVariable Long id,
            @RequestBody EventDtos.UpdateEventRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/assign-staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDtos.EventResponse> assignStaff(
            @PathVariable Long id,
            @RequestBody EventDtos.AssignStaffRequest request) {
        return ResponseEntity.ok(eventService.assignStaff(id, request));
    }

    @GetMapping("/{id}/eligible-staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffDtos.StaffResponse>> getEligibleStaff(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean differentAbledOnly) {
        return ResponseEntity.ok(eventService.getEligibleStaffForEvent(id, differentAbledOnly));
    }

    @GetMapping("/eligible-staff-preview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffDtos.StaffResponse>> getEligibleStaffPreview(
            @RequestParam String skills,
            @RequestParam String eventTimeStart,
            @RequestParam String eventTimeEnd,
            @RequestParam(defaultValue = "false") boolean differentAbledOnly) {
    	System.out.println("*************************");
       // OffsetDateTime start = OffsetDateTime.parse(eventTimeStart);
       // OffsetDateTime end = OffsetDateTime.parse(eventTimeEnd);
    	
    	LocalDateTime start = LocalDateTime.parse(eventTimeStart);
        LocalDateTime end = LocalDateTime.parse(eventTimeEnd);
        
        //LocalDateTime startNew = start.toLocalDateTime();
        //LocalDateTime endNew = end.toLocalDateTime();
        
        
        return ResponseEntity.ok(eventService.getEligibleStaffForSkills(Arrays.asList(skills.split(",")), start, end, differentAbledOnly));
    }

    @PostMapping("/{id}/duplicate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDtos.EventResponse> duplicateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventDtos.DuplicateEventRequest request) {
        return ResponseEntity.ok(eventService.duplicateEvent(id, request));
    }

    @PostMapping("/{id}/feedback")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> submitFeedback(
            @PathVariable Long id,
            @RequestBody EventDtos.FeedbackRequest request,
            HttpServletRequest httpRequest) {
        Long studentId = extractUserId(httpRequest);
        request.setStudentId(studentId);
        eventService.submitFeedback(id, request);
        return ResponseEntity.ok().build();
    }

    // ── STUDENT endpoints ──────────────────────────────────────────

    @GetMapping("/available")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EventDtos.EventResponse>> getAvailableEvents(HttpServletRequest httpRequest) {
        Long studentId = extractUserId(httpRequest);
        return ResponseEntity.ok(eventService.getAvailableEventsForStudent(studentId));
    }

    @GetMapping("/all-events")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EventDtos.EventResponse>> getAllEventsForStudent(HttpServletRequest httpRequest) {
        Long studentId = extractUserId(httpRequest);
        return ResponseEntity.ok(eventService.getAllEventsForStudent(studentId));
    }

    @PostMapping("/{id}/register")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EventDtos.EventResponse> registerForEvent(
            @PathVariable Long id, HttpServletRequest httpRequest) {
        Long studentId = extractUserId(httpRequest);
        return ResponseEntity.ok(eventService.registerStudent(id, studentId));
    }

    @DeleteMapping("/{id}/register")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EventDtos.EventResponse> unregisterFromEvent(
            @PathVariable Long id, HttpServletRequest httpRequest) {
        Long studentId = extractUserId(httpRequest);
        return ResponseEntity.ok(eventService.unregisterStudent(id, studentId));
    }

    @GetMapping("/my-events")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EventDtos.EventResponse>> getMyEvents(HttpServletRequest httpRequest) {
        Long studentId = extractUserId(httpRequest);
        return ResponseEntity.ok(eventService.getStudentEvents(studentId));
    }

    // ── helpers ────────────────────────────────────────────────────
    private Long extractUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return jwtUtil.extractUserId(token);
    }
}
