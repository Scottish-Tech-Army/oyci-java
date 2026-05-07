package org.scottishtecharmy.oyci.controllers;

import jakarta.validation.Valid;
import org.scottishtecharmy.oyci.dto.EventTypeCreateRequest;
import org.scottishtecharmy.oyci.dto.EventTypeQualificationRequest;
import org.scottishtecharmy.oyci.dto.EventTypeQualificationResponse;
import org.scottishtecharmy.oyci.dto.EventTypeResponse;
import org.scottishtecharmy.oyci.entities.EventType;
import org.scottishtecharmy.oyci.entities.EventTypeQualification;
import org.scottishtecharmy.oyci.entities.Qualification;
import org.scottishtecharmy.oyci.repositories.EventTypeQualificationRepository;
import org.scottishtecharmy.oyci.repositories.EventTypeRepository;
import org.scottishtecharmy.oyci.repositories.QualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private EventTypeQualificationRepository eventTypeQualificationRepository;

    // ─── Create ──────────────────────────────────────────────────────────────

    /**
     * Create an event type with optional qualification requirements.
     * POST /api/event-types
     */
    @PostMapping
    @Transactional
    public ResponseEntity<EventTypeResponse> create(
            @Valid @RequestBody EventTypeCreateRequest request) {
        validateQualifications(request.getRequiredQualifications());

        EventType eventType = new EventType(request.getName(), request.getDescription(),
                request.getDefDurMins(), request.getRequiredExperienceMonths());
        EventType saved = eventTypeRepository.save(eventType);
        saveQualificationRequirements(saved, request.getRequiredQualifications());

        return new ResponseEntity<>(buildEventTypeResponse(saved), HttpStatus.CREATED);
    }

    // ─── Read all ─────────────────────────────────────────────────────────────

    /**
     * Get all event types with their qualification requirements.
     * GET /api/event-types
     */
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<EventTypeResponse>> getAll() {
        List<EventTypeResponse> list = eventTypeRepository.findAll()
                .stream()
                .map(this::buildEventTypeResponse)
                .toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // ─── Read by ID ───────────────────────────────────────────────────────────

    /**
     * Get event type by ID with qualification requirements.
     * GET /api/event-types/{id}
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<EventTypeResponse> getById(@PathVariable Long id) {
        Optional<EventType> eventType = eventTypeRepository.findById(id);
        return eventType
                .map(et -> new ResponseEntity<>(buildEventTypeResponse(et), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ─── Read by Name ────────────────────────���────────────────────────────────

    /**
     * Get event type by name with qualification requirements.
     * GET /api/event-types/name/{name}
     */
    @GetMapping("/name/{name}")
    @Transactional(readOnly = true)
    public ResponseEntity<EventTypeResponse> getByName(@PathVariable String name) {
        EventType eventType = eventTypeRepository.findByName(name);
        if (eventType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(buildEventTypeResponse(eventType), HttpStatus.OK);
    }

    // ─── Update ───────────────────────────────────────────────────────────────

    /**
     * Update event type name, description, duration, and qualification requirements.
     * PUT /api/event-types/{id}
     */
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<EventTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EventTypeCreateRequest request) {
        Optional<EventType> existing = eventTypeRepository.findById(id);
        if (existing.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        validateQualifications(request.getRequiredQualifications());

        EventType et = existing.get();
        et.setName(request.getName());
        et.setDescription(request.getDescription());
        et.setDefDurMins(request.getDefDurMins());
        et.setRequiredExperienceMonths(request.getRequiredExperienceMonths());
        EventType updated = eventTypeRepository.save(et);

        // Replace qualification requirements
        eventTypeQualificationRepository.deleteByIdEventTypeId(id);
        saveQualificationRequirements(updated, request.getRequiredQualifications());

        return new ResponseEntity<>(buildEventTypeResponse(updated), HttpStatus.OK);
    }

    // ─── Delete ───────────────────────────────────────────────────────────────

    /**
     * Delete event type and its qualification requirements.
     * DELETE /api/event-types/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!eventTypeRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventTypeQualificationRepository.deleteByIdEventTypeId(id);
        eventTypeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private void validateQualifications(List<EventTypeQualificationRequest> qualificationRequests) {
        Set<Long> seen = new HashSet<>();
        for (EventTypeQualificationRequest qr : qualificationRequests) {
            if (!seen.add(qr.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Duplicate qualification id: " + qr.getId());
            }
            if (!qualificationRepository.existsById(qr.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Qualification not found: " + qr.getId());
            }
        }
    }

    private void saveQualificationRequirements(EventType eventType,
                                               List<EventTypeQualificationRequest> requests) {
        List<EventTypeQualification> rows = requests.stream()
                .map(qr -> {
                    Qualification q = qualificationRepository.findById(qr.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Qualification not found: " + qr.getId()));
                    return new EventTypeQualification(eventType, q);
                })
                .toList();
        if (!rows.isEmpty()) {
            eventTypeQualificationRepository.saveAll(rows);
        }
    }

    private EventTypeResponse buildEventTypeResponse(EventType eventType) {
        EventTypeResponse response = new EventTypeResponse();
        response.setId(eventType.getId());
        response.setName(eventType.getName());
        response.setDescription(eventType.getDescription());
        response.setDefDurMins(eventType.getDefDurMins());
        response.setRequiredExperienceMonths(eventType.getRequiredExperienceMonths());
        response.setRequiredExperience(formatExperience(eventType.getRequiredExperienceMonths()));
        response.setRequiredQualifications(
                eventTypeQualificationRepository.findByIdEventTypeId(eventType.getId())
                        .stream()
                        .map(this::buildQualificationResponse)
                        .toList()
        );
        return response;
    }

    private EventTypeQualificationResponse buildQualificationResponse(EventTypeQualification etq) {
        EventTypeQualificationResponse r = new EventTypeQualificationResponse();
        r.setId(etq.getId().getQualificationId());
        r.setName(etq.getQualification().getName());
        return r;
    }

    private String formatExperience(Integer months) {
        int m = months == null ? 0 : months;
        return (m / 12) + " yrs " + (m % 12) + " months";
    }
}
