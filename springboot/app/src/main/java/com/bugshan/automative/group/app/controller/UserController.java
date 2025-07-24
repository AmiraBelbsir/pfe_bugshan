package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.UserDTO;
import com.bugshan.automative.group.app.model.City;
import com.bugshan.automative.group.app.model.Gender;
import com.bugshan.automative.group.app.model.Role;
import com.bugshan.automative.group.app.model.User;
import com.bugshan.automative.group.app.repository.UserRepository;
import com.bugshan.automative.group.app.service.UserService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAllUsers().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUserWithImage(@RequestParam(value = "file", required = false) MultipartFile file,
                                                 @RequestParam("fullName") String fullName,
                                                 @RequestParam("email") String email,
                                                 @RequestParam("username") String username,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("phoneNumber") String phoneNumber,
                                                 @RequestParam("gender") String gender,
                                                 @RequestParam("role") String role,
                                                 @RequestParam("city") String city,
                                                 @RequestParam("address") String address) {

        if (userService.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("field", "username", "message", "Ce nom d'utilisateur est déjà utilisé."));
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("field", "email", "message", "Cet email est déjà utilisé."));
        }

        if (userService.existsByPhone(phoneNumber)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("field", "phoneNumber", "message", "Ce numéro est déjà utilisé."));
        }

        // Create User object without hashing the password
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);  // Directly saving the password without hashing
        user.setPhoneNumber(phoneNumber);
        user.setGender(Gender.valueOf(gender.toUpperCase()));  // Enum handling
        user.setRole(Role.valueOf(role.toUpperCase()));  // Enum handling
        user.setCity(City.valueOf(city.toUpperCase()));  // Enum handling
        user.setAddress(address);
        user.setActive(true);  // Default to active

        try {
            if (file != null && !file.isEmpty()) {
                // Handle image upload
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                // Set the image URL in the user
                user.setImageUrl(fileName);
            }

            // Save the user to the database
            User savedUser = userService.addUser(user);

            // Return the saved user data as DTO
            return ResponseEntity.ok(new UserDTO(savedUser));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "active", required = false) Boolean active) {

        // Fetch the current user from the database
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User existingUser = existingUserOpt.get();

        // Check if the username has changed and if the new username already exists
        if (username != null && !existingUser.getUsername().equals(username)) {
            if (userRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("field", "username", "message", "Ce nom d'utilisateur est déjà utilisé."));
            }
        }

        // Check if the email has changed and if the new email already exists
        if (email != null && !existingUser.getEmail().equals(email)) {
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("field", "email", "message", "Cet email est déjà utilisé."));
            }
        }

        // Check if the phone number has changed and if it already exists
        if (phoneNumber != null && !existingUser.getPhoneNumber().equals(phoneNumber)) {
            if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("field", "phoneNumber", "message", "Ce numéro de téléphone est déjà utilisé."));
            }
        }

        // Update the user with the new values, only if they were provided (no null checks)
        if (fullName != null) existingUser.setFullName(fullName);
        if (email != null) existingUser.setEmail(email);
        if (username != null) existingUser.setUsername(username);
        if (phoneNumber != null) existingUser.setPhoneNumber(phoneNumber);
        if (password != null) existingUser.setPassword(password);  // Remember to hash the password before saving
        if (role != null) existingUser.setRole(Role.valueOf(role.toUpperCase()));  // Enum handling
        if (gender != null) existingUser.setGender(Gender.valueOf(gender.toUpperCase()));  // Enum handling
        if (city != null) existingUser.setCity(City.valueOf(city.toUpperCase()));  // Enum handling
        if (address != null) existingUser.setAddress(address);
        if (active != null) existingUser.setActive(active);

        // Handle image upload if provided
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                // Set the image URL in the user
                existingUser.setImageUrl(fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
            }
        }

        // Save the updated user in the database
        userRepository.save(existingUser);

        // Return the updated user
        return ResponseEntity.ok(existingUser);  // Return the updated user
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User utilisateur) {
        UserDTO foundUser = userService.findByEmailAndPassword(utilisateur.getEmail(), utilisateur.getPassword());

        if (foundUser != null) {
            // Return user type with the DTO
            return ResponseEntity.ok().body(foundUser);
        } else {
            // Return 401 if credentials are invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/cities")
    public List<String> getAllCities() {
        return Arrays.stream(City.values()).map(Enum::name).toList();
    }
}
