package com.oyci.service;

import com.oyci.dto.StaffDtos;
import com.oyci.model.*;
import com.oyci.repository.EventRepository;
import com.oyci.repository.StaffRepository;
import com.oyci.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_WITHDRAWALS_PER_MONTH = 3;

    public StaffDtos.StaffResponse createStaff(StaffDtos.CreateStaffRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken: " + request.getUsername());
        }
        Staff staff = new Staff();
        staff.setUsername(request.getUsername());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setRole(Role.STAFF);
        staff.setName(request.getName());
        staff.setEmail(request.getEmail());
        staff.setTimeAvailable(request.getTimeAvailable());
        staff.setSkills(request.getSkills() == null ? new ArrayList<>() : new ArrayList<>(request.getSkills()));
        staff.setSupportDifferentlyAbled(request.isSupportDifferentlyAbled());
        staffRepository.save(staff);
        return toResponse(staff);
    }

    public List<StaffDtos.StaffResponse> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public StaffDtos.StaffResponse getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + id));
        return toResponse(staff);
    }

    public List<StaffDtos.StaffResponse> getStaffBySkills(List<String> skills) {
        return staffRepository.findBySkillsIn(skills).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StaffDtos.StaffResponse updateStaff(Long id, StaffDtos.UpdateStaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + id));
        if (request.getName() != null) staff.setName(request.getName());
        if (request.getEmail() != null) staff.setEmail(request.getEmail());
        if (request.getTimeAvailable() != null) staff.setTimeAvailable(request.getTimeAvailable());
        if (request.getSkills() != null) staff.setSkills(new ArrayList<>(request.getSkills()));
        if (request.getSupportDifferentlyAbled() != null) staff.setSupportDifferentlyAbled(request.getSupportDifferentlyAbled());
        staffRepository.save(staff);
        return toResponse(staff);
    }

    @Transactional
    public StaffDtos.StaffResponse selfUpdate(Long staffId, StaffDtos.StaffSelfUpdateRequest request) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));
        if (request.getSkills() != null) staff.setSkills(new ArrayList<>(request.getSkills()));
        if (request.getTimeAvailable() != null) staff.setTimeAvailable(request.getTimeAvailable());
        staffRepository.save(staff);
        return toResponse(staff);
    }

    @Transactional
    public StaffDtos.StaffResponse addHoliday(Long staffId, String holidayDate) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));
        if (!staff.getHolidays().contains(holidayDate)) {
            staff.getHolidays().add(holidayDate);
        }
        staffRepository.save(staff);
        return toResponse(staff);
    }

    @Transactional
    public StaffDtos.StaffResponse removeHoliday(Long staffId, String holidayDate) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));
        staff.getHolidays().remove(holidayDate);
        staffRepository.save(staff);
        return toResponse(staff);
    }

    @Transactional
    public void withdrawFromEvent(Long staffId, Long eventId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        String monthKey = YearMonth.now().toString(); // e.g. "2025-08"
        int currentWithdrawals = getWithdrawalsForMonth(staff, monthKey);
        if (currentWithdrawals >= MAX_WITHDRAWALS_PER_MONTH) {
            throw new RuntimeException("You have reached the maximum of " + MAX_WITHDRAWALS_PER_MONTH + " withdrawals this month.");
        }

        // Remove staff from event
        event.getAssignedStaff().removeIf(s -> s.getId().equals(staffId));
        event.getOptionalStaff().removeIf(s -> s.getId().equals(staffId));
        eventRepository.save(event);

        // Update withdrawal count
        updateWithdrawalCount(staff, monthKey, currentWithdrawals + 1);
        staffRepository.save(staff);
    }

    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    public double getWeeklyHours(Long staffId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusDays(7);
        List<Event> events = eventRepository.findEventsByStaffAndDateRange(staffId, weekStart, weekEnd);
        return events.stream().mapToDouble(Event::getDurationHours).sum();
    }

    public int getWithdrawalsThisMonth(Staff staff) {
        return getWithdrawalsForMonth(staff, YearMonth.now().toString());
    }

    private int getWithdrawalsForMonth(Staff staff, String monthKey) {
        return staff.getWithdrawalRecords().stream()
                .filter(r -> r.startsWith(monthKey + ":"))
                .mapToInt(r -> {
                    try { return Integer.parseInt(r.split(":")[1]); } catch (Exception e) { return 0; }
                }).sum();
    }

    private void updateWithdrawalCount(Staff staff, String monthKey, int newCount) {
        staff.getWithdrawalRecords().removeIf(r -> r.startsWith(monthKey + ":"));
        staff.getWithdrawalRecords().add(monthKey + ":" + newCount);
    }

    public StaffDtos.StaffResponse toResponse(Staff staff) {
        double weeklyHours = staff.getId() != null ? getWeeklyHours(staff.getId()) : 0;
        int withdrawals = getWithdrawalsThisMonth(staff);
        return new StaffDtos.StaffResponse(
                staff.getId(),
                staff.getUsername(),
                staff.getName(),
                staff.getEmail(),
                staff.getTimeAvailable(),
                staff.getSkills(),
                weeklyHours,
                staff.isSupportDifferentlyAbled(),
                staff.getHolidays() == null ? new ArrayList<>() : staff.getHolidays(),
                withdrawals
        );
    }
}
