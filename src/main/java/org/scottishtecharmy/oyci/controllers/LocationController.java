package org.scottishtecharmy.oyci.controllers;

import org.scottishtecharmy.oyci.entities.Location;
import org.scottishtecharmy.oyci.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    // Create
    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location) {
        Location savedLocation = locationRepository.save(location);
        return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
    }

    // Read all
    @GetMapping
    public ResponseEntity<List<Location>> getAll() {
        List<Location> locations = locationRepository.findAll();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    // Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getById(@PathVariable Long id) {
        Optional<Location> location = locationRepository.findById(id);
        return location.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Read by Name
    @GetMapping("/name/{name}")
    public ResponseEntity<Location> getByName(@PathVariable String name) {
        Location location = locationRepository.findByName(name);
        if (location != null) {
            return new ResponseEntity<>(location, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable Long id, @RequestBody Location location) {
        Optional<Location> existingLocation = locationRepository.findById(id);
        if (existingLocation.isPresent()) {
            Location loc = existingLocation.get();
            if (location.getName() != null) loc.setName(location.getName());
            if (location.getAddress() != null) loc.setAddress(location.getAddress());
            Location updatedLocation = locationRepository.save(loc);
            return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

