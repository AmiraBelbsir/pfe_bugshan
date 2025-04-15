package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.VehicleDTO;
import com.bugshan.automative.group.app.model.*;
import com.bugshan.automative.group.app.repository.VehicleRepository;
import com.bugshan.automative.group.app.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;

    // Get all vehicles
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.findAllVehicles();
    }

    // Get vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleService.findVehicleById(id);
        return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new vehicle with optional image
    @PostMapping
    public ResponseEntity<?> createVehicleWithImage(@RequestParam(value = "file", required = false) MultipartFile file,
                                                    @RequestParam(value = "additionalFiles", required = false) List<MultipartFile> additionalFiles,  // New parameter for additional photos
                                                    @RequestParam("make") String make,
                                                    @RequestParam("model") String model,
                                                    @RequestParam("quantity") int quantity,
                                                    @RequestParam("year") int year,
                                                    @RequestParam("color") String color,
                                                    @RequestParam("vin") String vin,
                                                    @RequestParam("mileage") int mileage,
                                                    @RequestParam("seats") int seats,
                                                    @RequestParam("vehicleType") String vehicleType,
                                                    @RequestParam("vehicleCondition") String vehicleCondition,
                                                    @RequestParam("retailPrice") double retailPrice,
                                                    @RequestParam("location") String location,
                                                    @RequestParam("insured") boolean insured,
                                                    @RequestParam("fuelLevel") double fuelLevel,
                                                    @RequestParam("fuelType") String fuelType,
                                                    @RequestParam("transmissionType") String transmissionType) {

        // Check for unique VIN
        if (vehicleService.existsByVin(vin)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("field", "vin", "message", "Un véhicule portant le même VIN existe déjà."));
        }

        // Create the Vehicle object
        Vehicle vehicle = new Vehicle();
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setQuantity(quantity);
        vehicle.setYear(year);
        vehicle.setColor(color);
        vehicle.setVin(vin);
        vehicle.setMileage(mileage);
        vehicle.setSeats(seats);
        vehicle.setVehicleType(VehicleType.valueOf(vehicleType.toUpperCase()));
        vehicle.setRetailPrice(retailPrice);
        vehicle.setLocation(location);
        vehicle.setInsured(insured);
        vehicle.setFuelLevel(fuelLevel);
        vehicle.setFuelType(FuelType.valueOf(fuelType.toUpperCase()));
        vehicle.setTransmissionType(TransmissionType.valueOf(transmissionType.toUpperCase()));
        vehicle.setVehicleCondition(VehicleCondition.valueOf(vehicleCondition.toUpperCase()));

        try {
            // Handle main image upload
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                vehicle.setImageUrl(fileName);  // Set the main image URL
            }

            // Handle additional images upload
            if (additionalFiles != null && !additionalFiles.isEmpty()) {
                for (MultipartFile additionalFile : additionalFiles) {
                    String additionalFileName = UUID.randomUUID() + "_" + additionalFile.getOriginalFilename();
                    Path additionalFilePath = Paths.get("uploads", additionalFileName);
                    Files.createDirectories(additionalFilePath.getParent());
                    Files.write(additionalFilePath, additionalFile.getBytes());

                    // Create and save VehiclePhoto
                    VehiclePhoto vehiclePhoto = new VehiclePhoto();
                    vehiclePhoto.setPhotoUrl(additionalFileName);
                    vehiclePhoto.setVehicle(vehicle);  // Link the photo to the vehicle
                    vehicle.getAdditionalPhotos().add(vehiclePhoto.getPhotoUrl());  // Add the photo to the vehicle
                }
            }

            // Save the vehicle
            Vehicle savedVehicle = vehicleService.addVehicle(vehicle);

            // Return the saved vehicle data as DTO
            return ResponseEntity.ok(new VehicleDTO(savedVehicle));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

    // Update vehicle details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id,
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam(value = "additionalFiles", required = false) List<MultipartFile> additionalFiles,  // New parameter for additional photos
                                           @RequestParam(value = "make", required = false) String make,
                                           @RequestParam(value = "model", required = false) String model,
                                           @RequestParam(value = "quantity", required = false) int quantity,
                                           @RequestParam(value = "year", required = false) Integer year,
                                           @RequestParam(value = "color", required = false) String color,
                                           @RequestParam(value = "vin", required = false) String vin,
                                           @RequestParam(value = "mileage", required = false) Integer mileage,
                                           @RequestParam(value = "seats", required = false) Integer seats,
                                           @RequestParam(value = "vehicleType", required = false) String vehicleType,
                                           @RequestParam(value=  "vehicleCondition", required = false) String vehicleCondition,
                                           @RequestParam(value = "retailPrice", required = false) Double retailPrice,
                                           @RequestParam(value = "location", required = false) String location,
                                           @RequestParam(value = "insured", required = false) Boolean insured,
                                           @RequestParam(value = "fuelLevel", required = false) Double fuelLevel,
                                           @RequestParam(value = "fuelType", required = false) String fuelType,
                                           @RequestParam(value = "transmissionType", required = false) String transmissionType) {

        Optional<Vehicle> existingVehicleOpt = vehicleRepository.findById(id);
        if (!existingVehicleOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Vehicle existingVehicle = existingVehicleOpt.get();

        // If license plate has changed, check if it exists already
        if (vin != null && !existingVehicle.getVin().equals(vin)) {
            if (vehicleRepository.findByVin(vin).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("field", "vin", "message", "Un véhicule portant le même VIN existe déjà."));
            }
        }

        // Update the vehicle with the new values, only if they were provided
        if (make != null) existingVehicle.setMake(make);
        if (model != null) existingVehicle.setModel(model);
        if (model != null) existingVehicle.setQuantity(quantity);
        if (year != null) existingVehicle.setYear(year);
        if (color != null) existingVehicle.setColor(color);
        if (vin != null) existingVehicle.setVin(vin);
        if (mileage != null) existingVehicle.setMileage(mileage);
        if (seats != null) existingVehicle.setSeats(seats);
        if (vehicleType != null) existingVehicle.setVehicleType(VehicleType.valueOf(vehicleType.toUpperCase()));
        if (retailPrice != null) existingVehicle.setRetailPrice(retailPrice);
        if (location != null) existingVehicle.setLocation(location);
        if (insured != null) existingVehicle.setInsured(insured);
        if (fuelLevel != null) existingVehicle.setFuelLevel(fuelLevel);
        if (fuelType != null) existingVehicle.setFuelType(FuelType.valueOf(fuelType.toUpperCase()));
        if (transmissionType != null) existingVehicle.setTransmissionType(TransmissionType.valueOf(transmissionType.toUpperCase()));
        if (vehicleCondition != null) existingVehicle.setVehicleCondition(VehicleCondition.valueOf(vehicleCondition.toUpperCase()));

        // Handle image upload if provided
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                existingVehicle.setImageUrl(fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
            }
        }

        // Handle additional images upload
        if (additionalFiles != null && !additionalFiles.isEmpty()) {
            for (MultipartFile additionalFile : additionalFiles) {
                String additionalFileName = UUID.randomUUID() + "_" + additionalFile.getOriginalFilename();
                Path additionalFilePath = Paths.get("uploads", additionalFileName);
                try {
                    Files.createDirectories(additionalFilePath.getParent());
                    Files.write(additionalFilePath, additionalFile.getBytes());

                    // Create and save VehiclePhoto
                    VehiclePhoto vehiclePhoto = new VehiclePhoto();
                    vehiclePhoto.setPhotoUrl(additionalFileName);
                    vehiclePhoto.setVehicle(existingVehicle);  // Link the photo to the vehicle
                    existingVehicle.getAdditionalPhotos().add(vehiclePhoto.getPhotoUrl());  // Add the photo to the vehicle
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Additional photo upload failed");
                }
            }
        }

        // Save the updated vehicle in the database
        vehicleRepository.save(existingVehicle);

        // Return the updated vehicle
        return ResponseEntity.ok(existingVehicle);
    }


    // Delete a vehicle by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
