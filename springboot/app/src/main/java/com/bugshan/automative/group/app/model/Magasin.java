
package com.bugshan.automative.group.app.model;

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
public class Magasin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String adresse;


    private boolean actif = true; // ➕ Champ pour activer/désactiver un magasin

    // 🔗 Un magasin peut avoir plusieurs pièces
    @OneToMany(mappedBy = "magasin", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Piece> pieces;

    // 🔗 Un magasin peut avoir plusieurs blocs
    @OneToMany(mappedBy = "magasin", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Bloc> blocs;
}
