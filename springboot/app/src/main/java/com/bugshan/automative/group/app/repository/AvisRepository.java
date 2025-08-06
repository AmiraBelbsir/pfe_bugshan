package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {

}