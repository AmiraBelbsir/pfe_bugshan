package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.VehicleDTO;
import com.bugshan.automative.group.app.model.Vehicle;
import com.bugshan.automative.group.app.model.VehicleType;
import com.bugshan.automative.group.app.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository VehicleRepository;

    public VehicleService(VehicleRepository VehicleRepository) {

        this.VehicleRepository = VehicleRepository;
    }

    public Vehicle addVehicle(Vehicle Vehicle) {
        return VehicleRepository.save(Vehicle);
    }

    // Find a Vehicle by ID
    public Optional<Vehicle> findVehicleById(Long id) {
        return VehicleRepository.findById(id);
    }


    // Get all Vehicles
    public List<Vehicle> findAllVehicles() {
        return VehicleRepository.findAll();
    }




}