package org.scottishtecharmy.oyci.controllers;

import org.scottishtecharmy.oyci.entities.EventTypeQualification;
import org.scottishtecharmy.oyci.entities.EventTypeQualificationId;
import org.scottishtecharmy.oyci.repositories.EventTypeQualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event-type-qualifications")
public class EventTypeQualificationController {

    @Autowired
    private EventTypeQualificationRepository eventTypeQualificationRepository;

    // Create
    @PostMapping
    public ResponseEntity<EventTypeQualification> create(@RequestBody EventTypeQualification eventTypeQualification) {
        EventTypeQualification savedEventTypeQualification = eventTypeQualificationRepository.save(eventTypeQualification);
        return new ResponseEntity<>(savedEventTypeQualification, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<EventTypeQualification>> getAll() {
        List<EventTypeQualification> eventTypeQualifications = eventTypeQualificationRepository.findAll();
        return new ResponseEntity<>(eventTypeQualifications, HttpStatus.OK);
    }

    // Read by Event Type ID
    @GetMapping("/event-type/{eventTypeId}")
    public ResponseEntity<List<EventTypeQualification>> getByEventTypeId(@PathVariable Long eventTypeId) {
        List<EventTypeQualification> eventTypeQualifications = eventTypeQualificationRepository.findByIdEventTypeId(eventTypeId);
        return new ResponseEntity<>(eventTypeQualifications, HttpStatus.OK);
    }

    // Read by Qualification ID
    @GetMapping("/qualification/{qualificationId}")
    public ResponseEntity<List<EventTypeQualification>> getByQualificationId(@PathVariable Long qualificationId) {
        List<EventTypeQualification> eventTypeQualifications = eventTypeQualificationRepository.findByIdQualificationId(qualificationId);
        return new ResponseEntity<>(eventTypeQualifications, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{eventTypeId}/{qualificationId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventTypeId, @PathVariable Long qualificationId) {
        EventTypeQualificationId id = new EventTypeQualificationId(eventTypeId, qualificationId);
        if (eventTypeQualificationRepository.existsById(id)) {
            eventTypeQualificationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

