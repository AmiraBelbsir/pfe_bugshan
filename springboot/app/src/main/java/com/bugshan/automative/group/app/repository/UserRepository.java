package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String gsm);

    Optional<Object> findByUsername(String username);

    Optional<Object> findByPhoneNumber(String phoneNumber);
}