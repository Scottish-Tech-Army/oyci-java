package org.scottishtecharmy.oyci.controllers;

import jakarta.validation.Valid;
import org.scottishtecharmy.oyci.dto.StaffAvailabilityEventInstanceResponse;
import org.scottishtecharmy.oyci.dto.StaffAvailabilityResponse;
import org.scottishtecharmy.oyci.dto.StaffCreateRequest;
import org.scottishtecharmy.oyci.dto.StaffHolidayRequest;
import org.scottishtecharmy.oyci.dto.StaffHolidayResponse;
import org.scottishtecharmy.oyci.dto.StaffQualificationRequest;
import org.scottishtecharmy.oyci.dto.StaffQualificationResponse;
import org.scottishtecharmy.oyci.dto.StaffResponse;
import org.scottishtecharmy.oyci.entities.*;
import org.scottishtecharmy.oyci.repositories.EventAssignmentRepository;
import org.scottishtecharmy.oyci.repositories.EventTypeRepository;
import org.scottishtecharmy.oyci.repositories.EventTypeQualificationRepository;
import org.scottishtecharmy.oyci.repositories.QualificationRepository;
import org.scottishtecharmy.oyci.repositories.StaffHolidayRepository;
import org.scottishtecharmy.oyci.repositories.StaffQualificationRepository;
import org.scottishtecharmy.oyci.repositories.StaffRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffRepository staffRepository;
    private final QualificationRepository qualificationRepository;
    private final StaffQualificationRepository staffQualificationRepository;
    private final StaffHolidayRepository staffHolidayRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EventTypeQualificationRepository eventTypeQualificationRepository;
    private final EventAssignmentRepository eventAssignmentRepository;

    public StaffController(StaffRepository staffRepository,
                           QualificationRepository qualificationRepository,
                           StaffQualificationRepository staffQualificationRepository,
                           StaffHolidayRepository staffHolidayRepository,
                           EventTypeRepository eventTypeRepository,
                           EventTypeQualificationRepository eventTypeQualificationRepository,
                           EventAssignmentRepository eventAssignmentRepository) {
        this.staffRepository = staffRepository;
        this.qualificationRepository = qualificationRepository;
        this.staffQualificationRepository = staffQualificationRepository;
        this.staffHolidayRepository = staffHolidayRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.eventTypeQualificationRepository = eventTypeQualificationRepository;
        this.eventAssignmentRepository = eventAssignmentRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<StaffResponse> create(@Valid @RequestBody StaffCreateRequest request) {
        validateRequest(request, null);

        Staff staff = new Staff(request.getName(), request.getEmail(), request.getWeeklyAvailHours());
        staff.setExperienceMonths(request.getExperienceMonths());
        staff.setDesignation(request.getDesignation());
        Staff savedStaff = staffRepository.save(staff);
        saveAssociations(savedStaff, request);

        return new ResponseEntity<>(buildStaffResponse(savedStaff), HttpStatus.CREATED);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<StaffResponse>> getAll() {
        List<StaffResponse> staffList = staffRepository.findAll()
                .stream()
                .map(this::buildStaffResponse)
                .toList();
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<StaffResponse> getById(@PathVariable Long id) {
        Optional<Staff> staff = staffRepository.findById(id);
        return staff.map(value -> new ResponseEntity<>(buildStaffResponse(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email}")
    @Transactional
    public ResponseEntity<StaffResponse> getByEmail(@PathVariable String email) {
        Staff staff = staffRepository.findByEmail(email);
        if (staff != null) {
            return new ResponseEntity<>(buildStaffResponse(staff), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/available-for-event")
    @Transactional(readOnly = true)
    public ResponseEntity<List<StaffAvailabilityResponse>> getAvailableForEvent(
            @RequestParam Long eventTypeId,
            @RequestParam LocalDateTime startTime) {

        LocalDate requestedDate = startTime.toLocalDate();

        EventType eventType = eventTypeRepository.findById(eventTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Event type not found: " + eventTypeId));

        List<EventTypeQualification> eventRequirements = eventTypeQualificationRepository
                .findByIdEventTypeId(eventTypeId);

        Set<Long> requiredQualificationIds = new HashSet<>();
        for (EventTypeQualification requirement : eventRequirements) {
            requiredQualificationIds.add(requirement.getId().getQualificationId());
        }

        int maxRequiredExperienceMonths = eventType.getRequiredExperienceMonths() != null
                ? eventType.getRequiredExperienceMonths() : 0;

        Set<Long> staffOnHoliday = new HashSet<>(staffHolidayRepository.findStaffIdsOnHoliday(requestedDate));

        Map<Long, Set<Long>> staffQualificationIds = new HashMap<>();
        for (StaffQualification staffQualification : staffQualificationRepository.findAll()) {
            staffQualificationIds
                    .computeIfAbsent(staffQualification.getId().getStaffId(), ignored -> new HashSet<>())
                    .add(staffQualification.getId().getQualificationId());
        }

        LocalDate weekEndDate = requestedDate.with(DayOfWeek.SUNDAY);
        LocalDateTime weekEnd = weekEndDate.atStartOfDay();
        LocalDateTime weekStart = weekEnd.minusDays(7);


        List<Staff> eligibleStaff = staffRepository.findAll().stream()
                .filter(staff -> !staffOnHoliday.contains(staff.getId()))
                .filter(staff -> matchesAnyQualificationAndExperience(
                        staff,
                        staffQualificationIds.getOrDefault(staff.getId(), Collections.emptySet()),
                        requiredQualificationIds,
                        maxRequiredExperienceMonths))
                .toList();

        Map<Long, List<StaffAvailabilityEventInstanceResponse>> weeklyEventsByStaff = new HashMap<>();
        if (!eligibleStaff.isEmpty()) {
            List<Long> eligibleStaffIds = eligibleStaff.stream().map(Staff::getId).toList();
            for (EventAssignment assignment : eventAssignmentRepository.findAssignmentsForStaffInWeek(eligibleStaffIds, weekStart, weekEnd)) {
                weeklyEventsByStaff
                        .computeIfAbsent(assignment.getStaff().getId(), ignored -> new java.util.ArrayList<>())
                        .add(buildAvailabilityEventInstanceResponse(assignment.getEventInstance()));
            }
        }

        List<StaffAvailabilityResponse> response = eligibleStaff.stream()
                .map(staff -> buildStaffAvailabilityResponse(
                        staff,
                        staffQualificationIds.getOrDefault(staff.getId(), Collections.emptySet()),
                        weeklyEventsByStaff.getOrDefault(staff.getId(), Collections.emptyList())))
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<StaffResponse> update(@PathVariable Long id, @Valid @RequestBody StaffCreateRequest request) {
        Optional<Staff> existingStaff = staffRepository.findById(id);
        if (existingStaff.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        validateRequest(request, id);

        Staff staff = existingStaff.get();
        staff.setName(request.getName());
        staff.setEmail(request.getEmail());
        staff.setWeeklyAvailHours(request.getWeeklyAvailHours());
        staff.setExperienceMonths(request.getExperienceMonths());
        staff.setDesignation(request.getDesignation());
        Staff updatedStaff = staffRepository.save(staff);

        staffQualificationRepository.deleteByIdStaffId(id);
        staffHolidayRepository.deleteByStaffId(id);
        saveAssociations(updatedStaff, request);

        return new ResponseEntity<>(buildStaffResponse(updatedStaff), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (staffRepository.existsById(id)) {
            staffQualificationRepository.deleteByIdStaffId(id);
            staffHolidayRepository.deleteByStaffId(id);
            staffRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void validateRequest(StaffCreateRequest request, Long currentStaffId) {
        Staff staffByEmail = staffRepository.findByEmail(request.getEmail());
        if (staffByEmail != null && (currentStaffId == null || !staffByEmail.getId().equals(currentStaffId))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address already exists");
        }

        Set<Long> qualificationIds = new HashSet<>();
        for (StaffQualificationRequest qualificationRequest : request.getQualifications()) {
            if (!qualificationIds.add(qualificationRequest.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Duplicate qualification id: " + qualificationRequest.getId());
            }

            if (!qualificationRepository.existsById(qualificationRequest.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Qualification not found: " + qualificationRequest.getId());
            }
        }

        for (StaffHolidayRequest holidayRequest : request.getHolidays()) {
            if (holidayRequest.getEndDate().isBefore(holidayRequest.getStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Holiday end date cannot be before start date");
            }
        }
    }

    private void saveAssociations(Staff staff, StaffCreateRequest request) {
        List<StaffQualification> qualifications = request.getQualifications()
                .stream()
                .map(qualificationRequest -> buildStaffQualification(staff, qualificationRequest))
                .toList();
        if (!qualifications.isEmpty()) {
            staffQualificationRepository.saveAll(qualifications);
        }

        List<StaffHoliday> holidays = request.getHolidays()
                .stream()
                .map(holidayRequest -> new StaffHoliday(staff, holidayRequest.getStartDate(), holidayRequest.getEndDate()))
                .toList();
        if (!holidays.isEmpty()) {
            staffHolidayRepository.saveAll(holidays);
        }
    }

    private StaffQualification buildStaffQualification(Staff staff, StaffQualificationRequest qualificationRequest) {
        Qualification qualification = qualificationRepository.findById(qualificationRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Qualification not found: " + qualificationRequest.getId()));
        return new StaffQualification(staff, qualification);
    }

    private StaffResponse buildStaffResponse(Staff staff) {
        StaffResponse response = new StaffResponse();
        response.setId(staff.getId());
        response.setName(staff.getName());
        response.setEmail(staff.getEmail());
        response.setWeeklyAvailHours(staff.getWeeklyAvailHours());
        response.setExperienceMonths(staff.getExperienceMonths());
        response.setExperience(formatExperience(staff.getExperienceMonths()));
        response.setDesignation(staff.getDesignation());
        response.setQualifications(staffQualificationRepository.findByIdStaffId(staff.getId())
                .stream()
                .map(this::buildQualificationResponse)
                .toList());
        response.setHolidays(staffHolidayRepository.findByStaffId(staff.getId())
                .stream()
                .map(this::buildHolidayResponse)
                .toList());
        return response;
    }

    private StaffAvailabilityResponse buildStaffAvailabilityResponse(
            Staff staff,
            Set<Long> qualificationIds,
            List<StaffAvailabilityEventInstanceResponse> weeklyEventInstances) {
        StaffAvailabilityResponse response = new StaffAvailabilityResponse();
        response.setId(staff.getId());
        response.setName(staff.getName());
        response.setEmail(staff.getEmail());
        response.setDesignation(staff.getDesignation());
        response.setWeeklyAvailHours(staff.getWeeklyAvailHours());
        response.setExperienceMonths(staff.getExperienceMonths());
        response.setQualificationIds(qualificationIds.stream().sorted().toList());
        response.setWeeklyEventInstances(weeklyEventInstances);

        // Calculate weekly committed hours from event instances
        int weeklyCommittedHours = 0;
        for (StaffAvailabilityEventInstanceResponse event : weeklyEventInstances) {
            if (event.getStartTime() != null && event.getEndTime() != null) {
                long durationMinutes = java.time.temporal.ChronoUnit.MINUTES.between(
                        event.getStartTime(),
                        event.getEndTime());
                weeklyCommittedHours += (int) (durationMinutes / 60);
            }
        }
        response.setWeeklyCommittedHours(weeklyCommittedHours);

        // Calculate remaining hours
        int availableHours = staff.getWeeklyAvailHours() == null ? 0 : staff.getWeeklyAvailHours();
        int remainingHours = availableHours - weeklyCommittedHours;
        response.setRemainingHours(remainingHours);

        return response;
    }

    private StaffAvailabilityEventInstanceResponse buildAvailabilityEventInstanceResponse(EventInstance eventInstance) {
        StaffAvailabilityEventInstanceResponse response = new StaffAvailabilityEventInstanceResponse();
        response.setId(eventInstance.getId());
        response.setEventTypeId(eventInstance.getEventType().getId());
        response.setEventTypeName(eventInstance.getEventType().getName());
        response.setLocationId(eventInstance.getLocation().getId());
        response.setStartTime(eventInstance.getStartTime());
        response.setEndTime(eventInstance.getEndTime());
        response.setShiftStartTime(eventInstance.getShiftStartTime());
        response.setShiftEndTime(eventInstance.getShiftEndTime());
        return response;
    }

    private boolean matchesAnyQualificationAndExperience(
            Staff staff,
            Set<Long> staffQualificationIds,
            Set<Long> requiredQualificationIds,
            int requiredExperienceMonths) {
        boolean hasAnyRequiredQualification = requiredQualificationIds.isEmpty()
                || staffQualificationIds.stream().anyMatch(requiredQualificationIds::contains);
        int staffExperienceMonths = staff.getExperienceMonths() == null ? 0 : staff.getExperienceMonths();
        boolean hasRequiredExperience = staffExperienceMonths >= requiredExperienceMonths;
        return hasAnyRequiredQualification && hasRequiredExperience;
    }

    private StaffQualificationResponse buildQualificationResponse(StaffQualification staffQualification) {
        StaffQualificationResponse response = new StaffQualificationResponse();
        response.setId(staffQualification.getId().getQualificationId());
        response.setName(staffQualification.getQualification().getName());
        return response;
    }

    private StaffHolidayResponse buildHolidayResponse(StaffHoliday staffHoliday) {
        StaffHolidayResponse response = new StaffHolidayResponse();
        response.setId(staffHoliday.getId());
        response.setStartDate(staffHoliday.getStartDate());
        response.setEndDate(staffHoliday.getEndDate());
        return response;
    }

    private String formatExperience(Integer experienceMonths) {
        int months = experienceMonths == null ? 0 : experienceMonths;
        int years = months / 12;
        int remainingMonths = months % 12;
        return years + " yrs " + remainingMonths + " months";
    }
}
