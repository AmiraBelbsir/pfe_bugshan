package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.User;
import com.bugshan.automative.group.app.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Object> findByVin(String vin);

    boolean existsByVin(String vin);
}