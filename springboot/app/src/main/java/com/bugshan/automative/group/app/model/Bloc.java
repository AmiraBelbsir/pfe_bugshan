package com.bugshan.automative.group.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bloc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    // 🔗 Chaque bloc appartient à un seul magasin
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "magasin_id")
    private Magasin magasin;

    // 🔗 Chaque bloc peut contenir plusieurs pièces
    @OneToMany(mappedBy = "bloc", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Piece> pieces;
}

