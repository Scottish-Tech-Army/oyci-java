package org.scottishtecharmy.oyci.controllers;

import org.scottishtecharmy.oyci.entities.Qualification;
import org.scottishtecharmy.oyci.repositories.QualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/qualifications")
public class QualificationController {

    @Autowired
    private QualificationRepository qualificationRepository;

    // Create
    @PostMapping
    public ResponseEntity<Qualification> create(@RequestBody Qualification qualification) {
        Qualification savedQualification = qualificationRepository.save(qualification);
        return new ResponseEntity<>(savedQualification, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<Qualification>> getAll() {
        List<Qualification> qualifications = qualificationRepository.findAll();
        return new ResponseEntity<>(qualifications, HttpStatus.OK);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<Qualification> getById(@PathVariable Long id) {
        Optional<Qualification> qualification = qualificationRepository.findById(id);
        return qualification.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Read by Name
    @GetMapping("/name/{name}")
    public ResponseEntity<Qualification> getByName(@PathVariable String name) {
        Qualification qualification = qualificationRepository.findByName(name);
        if (qualification != null) {
            return new ResponseEntity<>(qualification, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Qualification> update(@PathVariable Long id, @RequestBody Qualification qualification) {
        Optional<Qualification> existingQualification = qualificationRepository.findById(id);
        if (existingQualification.isPresent()) {
            Qualification q = existingQualification.get();
            if (qualification.getName() != null) q.setName(qualification.getName());
            Qualification updatedQualification = qualificationRepository.save(q);
            return new ResponseEntity<>(updatedQualification, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (qualificationRepository.existsById(id)) {
            qualificationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

