package com.bugshan.automative.group.app.repository;


import com.bugshan.automative.group.app.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {


}