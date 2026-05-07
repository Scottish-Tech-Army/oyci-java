package org.scottishtecharmy.oyci.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.scottishtecharmy.oyci.dto.EventInstanceResponse;
import org.scottishtecharmy.oyci.entities.EventAssignment;
import org.scottishtecharmy.oyci.entities.EventInstance;
import org.scottishtecharmy.oyci.repositories.EventAssignmentRepository;
import org.scottishtecharmy.oyci.repositories.EventInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-instances")
public class EventInstanceController {

    @Autowired
    private EventInstanceRepository eventInstanceRepository;

    @Autowired
    private EventAssignmentRepository eventAssignmentRepository;

    // Create
    @PostMapping
    public ResponseEntity<EventInstance> create(@RequestBody EventInstance eventInstance) {
        EventInstance savedEventInstance = eventInstanceRepository.save(eventInstance);
        return new ResponseEntity<>(savedEventInstance, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<EventInstance>> getAll() {
        List<EventInstance> eventInstances = eventInstanceRepository.findAll();
        return new ResponseEntity<>(eventInstances, HttpStatus.OK);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventInstance> getById(@PathVariable Long id) {
        Optional<EventInstance> eventInstance = eventInstanceRepository.findById(id);
        return eventInstance.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Read by Event Type ID
    @GetMapping("/event-type/{eventTypeId}")
    public ResponseEntity<List<EventInstance>> getByEventTypeId(@PathVariable Long eventTypeId) {
        List<EventInstance> eventInstances = eventInstanceRepository.findByEventTypeId(eventTypeId);
        return new ResponseEntity<>(eventInstances, HttpStatus.OK);
    }

    // Read by Location ID
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<EventInstance>> getByLocationId(@PathVariable Long locationId) {
        List<EventInstance> eventInstances = eventInstanceRepository.findByLocationId(locationId);
        return new ResponseEntity<>(eventInstances, HttpStatus.OK);
    }

    /**
     * GET /api/event-instances/by-month?month=2026-03
     *
     * Returns all event instances for the given month (yyyy-MM format), ordered by start time.
     * If month is omitted, returns ALL event instances ordered by start time.
     * Returns an empty list if none are found — never 404.
     *
     * @param month optional month in yyyy-MM format, e.g. "2026-03"
     */
    @GetMapping("/by-month")
    @Operation(
        summary = "Get event instances by month",
        description = "Returns all event instances for the given month (yyyy-MM). " +
                      "Omit the month parameter to retrieve all event instances. " +
                      "Results are ordered by start time. Returns an empty list if none found."
    )
    public ResponseEntity<List<EventInstanceResponse>> getByMonth(
            @Parameter(description = "Month in yyyy-MM format, e.g. 2026-03. Omit to get all events.")
            @RequestParam(required = false) String month) {

        List<EventInstance> instances;

        if (month == null || month.isBlank()) {
            // No filter — return everything ordered by start time
            instances = eventInstanceRepository.findAllByOrderByStartTime();
        } else {
            // Parse and validate the month parameter
            YearMonth yearMonth;
            try {
                yearMonth = YearMonth.parse(month);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid month format '" + month + "'. Expected yyyy-MM, e.g. 2026-03");
            }

            LocalDateTime from = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime to   = yearMonth.atEndOfMonth().atTime(23, 59, 59);
            instances = eventInstanceRepository.findByStartTimeBetweenOrderByStartTime(from, to);
        }

        List<EventInstanceResponse> response = instances.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<EventInstance> update(@PathVariable Long id, @RequestBody EventInstance eventInstance) {
        Optional<EventInstance> existingEventInstance = eventInstanceRepository.findById(id);
        if (existingEventInstance.isPresent()) {
            EventInstance ei = existingEventInstance.get();
            if (eventInstance.getEventType() != null) ei.setEventType(eventInstance.getEventType());
            if (eventInstance.getLocation() != null) ei.setLocation(eventInstance.getLocation());
            if (eventInstance.getStartTime() != null) ei.setStartTime(eventInstance.getStartTime());
            if (eventInstance.getEndTime() != null) ei.setEndTime(eventInstance.getEndTime());
            ei.setShiftStartTime(eventInstance.getShiftStartTime());
            ei.setShiftEndTime(eventInstance.getShiftEndTime());
            EventInstance updatedEventInstance = eventInstanceRepository.save(ei);
            return new ResponseEntity<>(updatedEventInstance, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (eventInstanceRepository.existsById(id)) {
            eventInstanceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // ---- private helpers ----

    private EventInstanceResponse toResponse(EventInstance ei) {
        EventInstanceResponse r = new EventInstanceResponse();
        r.setId(ei.getId());

        r.setEventTypeId(ei.getEventType().getId());
        r.setEventTypeName(ei.getEventType().getName());
        r.setEventTypeDescription(ei.getEventType().getDescription());
        r.setDefDurMins(ei.getEventType().getDefDurMins());

        r.setLocationId(ei.getLocation().getId());
        r.setLocationName(ei.getLocation().getName());
        r.setLocationAddress(ei.getLocation().getAddress());

        r.setStartTime(ei.getStartTime());
        r.setEndTime(ei.getEndTime());
        r.setShiftStartTime(ei.getShiftStartTime());
        r.setShiftEndTime(ei.getShiftEndTime());

        List<EventInstanceResponse.AssignedStaffResponse> staff =
                eventAssignmentRepository.findByEventInstanceId(ei.getId())
                        .stream()
                        .map(this::toAssignedStaff)
                        .collect(Collectors.toList());
        r.setAssignedStaff(staff);

        return r;
    }

    private EventInstanceResponse.AssignedStaffResponse toAssignedStaff(EventAssignment ea) {
        EventInstanceResponse.AssignedStaffResponse s = new EventInstanceResponse.AssignedStaffResponse();
        s.setStaffId(ea.getStaff().getId());
        s.setStaffName(ea.getStaff().getName());
        s.setStaffEmail(ea.getStaff().getEmail());
        return s;
    }
}
