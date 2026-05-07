package org.scottishtecharmy.oyci.controllers;

import org.scottishtecharmy.oyci.entities.EventAssignment;
import org.scottishtecharmy.oyci.repositories.EventAssignmentRepository;
import org.scottishtecharmy.oyci.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event-assignments")
public class EventAssignmentController {

    @Autowired
    private EventAssignmentRepository eventAssignmentRepository;

    @Autowired
    private EmailService emailService;

    // Create
    @PostMapping
    public ResponseEntity<EventAssignment> create(@RequestBody EventAssignment eventAssignment) {
        EventAssignment savedEventAssignment = eventAssignmentRepository.save(eventAssignment);

        // Fire-and-forget: send assignment email asynchronously
//        emailService.sendAssignmentNotification(savedEventAssignment);

        return new ResponseEntity<>(savedEventAssignment, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<EventAssignment>> getAll() {
        List<EventAssignment> eventAssignments = eventAssignmentRepository.findAll();
        return new ResponseEntity<>(eventAssignments, HttpStatus.OK);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventAssignment> getById(@PathVariable Long id) {
        Optional<EventAssignment> eventAssignment = eventAssignmentRepository.findById(id);
        return eventAssignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Read by Event Instance ID
    @GetMapping("/event-instance/{eventInstanceId}")
    public ResponseEntity<List<EventAssignment>> getByEventInstanceId(@PathVariable Long eventInstanceId) {
        List<EventAssignment> eventAssignments = eventAssignmentRepository.findByEventInstanceId(eventInstanceId);
        return new ResponseEntity<>(eventAssignments, HttpStatus.OK);
    }

    // Read by Staff ID
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<EventAssignment>> getByStaffId(@PathVariable Long staffId) {
        List<EventAssignment> eventAssignments = eventAssignmentRepository.findByStaffId(staffId);
        return new ResponseEntity<>(eventAssignments, HttpStatus.OK);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<EventAssignment> update(@PathVariable Long id, @RequestBody EventAssignment eventAssignment) {
        Optional<EventAssignment> existingEventAssignment = eventAssignmentRepository.findById(id);
        if (existingEventAssignment.isPresent()) {
            EventAssignment ea = existingEventAssignment.get();
            if (eventAssignment.getEventInstance() != null) ea.setEventInstance(eventAssignment.getEventInstance());
            if (eventAssignment.getStaff() != null) ea.setStaff(eventAssignment.getStaff());
            EventAssignment updatedEventAssignment = eventAssignmentRepository.save(ea);
            return new ResponseEntity<>(updatedEventAssignment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (eventAssignmentRepository.existsById(id)) {
            eventAssignmentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

