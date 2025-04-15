package com.bugshan.automative.group.app.dto;


import com.bugshan.automative.group.app.model.City;
import com.bugshan.automative.group.app.model.Gender;
import com.bugshan.automative.group.app.model.Role;
import com.bugshan.automative.group.app.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Role role;
    private Gender gender;
    private String imageUrl;
    private boolean active;
    private String username;
    private String address;
    private City city;

    // Manual Constructor (if you prefer without Lombok)
    public UserDTO(User utilisateur) {
        this.id = utilisateur.getId();
        this.fullName = utilisateur.getFullName();
        this.phoneNumber = utilisateur.getPhoneNumber();
        this.email = utilisateur.getEmail();
        this.role = utilisateur.getRole();
        this.gender = utilisateur.getGender();
        this.imageUrl = utilisateur.getImageUrl();
        this.active = utilisateur.isActive();
        this.username = utilisateur.getUsername();
        this.address = utilisateur.getAddress();
        this.city = utilisateur.getCity();
    }

}