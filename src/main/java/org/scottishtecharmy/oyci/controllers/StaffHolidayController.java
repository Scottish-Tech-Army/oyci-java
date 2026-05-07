package org.scottishtecharmy.oyci.controllers;

import org.scottishtecharmy.oyci.entities.StaffHoliday;
import org.scottishtecharmy.oyci.repositories.StaffHolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff-holidays")
public class StaffHolidayController {

    @Autowired
    private StaffHolidayRepository staffHolidayRepository;

    // Create
    @PostMapping
    public ResponseEntity<StaffHoliday> create(@RequestBody StaffHoliday staffHoliday) {
        StaffHoliday savedStaffHoliday = staffHolidayRepository.save(staffHoliday);
        return new ResponseEntity<>(savedStaffHoliday, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<StaffHoliday>> getAll() {
        List<StaffHoliday> staffHolidays = staffHolidayRepository.findAll();
        return new ResponseEntity<>(staffHolidays, HttpStatus.OK);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<StaffHoliday> getById(@PathVariable Long id) {
        Optional<StaffHoliday> staffHoliday = staffHolidayRepository.findById(id);
        return staffHoliday.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Read by Staff ID
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffHoliday>> getByStaffId(@PathVariable Long staffId) {
        List<StaffHoliday> staffHolidays = staffHolidayRepository.findByStaffId(staffId);
        return new ResponseEntity<>(staffHolidays, HttpStatus.OK);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<StaffHoliday> update(@PathVariable Long id, @RequestBody StaffHoliday staffHoliday) {
        Optional<StaffHoliday> existingStaffHoliday = staffHolidayRepository.findById(id);
        if (existingStaffHoliday.isPresent()) {
            StaffHoliday sh = existingStaffHoliday.get();
            if (staffHoliday.getStaff() != null) sh.setStaff(staffHoliday.getStaff());
            if (staffHoliday.getStartDate() != null) sh.setStartDate(staffHoliday.getStartDate());
            if (staffHoliday.getEndDate() != null) sh.setEndDate(staffHoliday.getEndDate());
            StaffHoliday updatedStaffHoliday = staffHolidayRepository.save(sh);
            return new ResponseEntity<>(updatedStaffHoliday, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (staffHolidayRepository.existsById(id)) {
            staffHolidayRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

