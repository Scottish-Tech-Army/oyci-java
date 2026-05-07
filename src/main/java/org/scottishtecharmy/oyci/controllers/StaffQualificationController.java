package org.scottishtecharmy.oyci.controllers;

import org.scottishtecharmy.oyci.entities.StaffQualification;
import org.scottishtecharmy.oyci.entities.StaffQualificationId;
import org.scottishtecharmy.oyci.repositories.StaffQualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff-qualifications")
public class StaffQualificationController {

    @Autowired
    private StaffQualificationRepository staffQualificationRepository;

    // Create
    @PostMapping
    public ResponseEntity<StaffQualification> create(@RequestBody StaffQualification staffQualification) {
        StaffQualification savedStaffQualification = staffQualificationRepository.save(staffQualification);
        return new ResponseEntity<>(savedStaffQualification, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<StaffQualification>> getAll() {
        List<StaffQualification> staffQualifications = staffQualificationRepository.findAll();
        return new ResponseEntity<>(staffQualifications, HttpStatus.OK);
    }

    // Read by Staff ID
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffQualification>> getByStaffId(@PathVariable Long staffId) {
        List<StaffQualification> staffQualifications = staffQualificationRepository.findByIdStaffId(staffId);
        return new ResponseEntity<>(staffQualifications, HttpStatus.OK);
    }

    // Read by Qualification ID
    @GetMapping("/qualification/{qualificationId}")
    public ResponseEntity<List<StaffQualification>> getByQualificationId(@PathVariable Long qualificationId) {
        List<StaffQualification> staffQualifications = staffQualificationRepository.findByIdQualificationId(qualificationId);
        return new ResponseEntity<>(staffQualifications, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{staffId}/{qualificationId}")
    public ResponseEntity<Void> delete(@PathVariable Long staffId, @PathVariable Long qualificationId) {
        StaffQualificationId id = new StaffQualificationId(staffId, qualificationId);
        if (staffQualificationRepository.existsById(id)) {
            staffQualificationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

