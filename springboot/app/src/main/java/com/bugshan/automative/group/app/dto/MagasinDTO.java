package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.Magasin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class MagasinDTO {
    private Long id;
    private String nom;
    private String adresse;
    private List<BlocDTO> blocs;
    private List<String> nomsBlocs; // ou "blocsNoms", comme tu veux
    private boolean actif;
    public MagasinDTO(Magasin magasin) {
        this.id = magasin.getId();
        this.nom = magasin.getNom();
        this.adresse = magasin.getAdresse();
        this.blocs = magasin.getBlocs() != null
                ? magasin.getBlocs().stream().map(BlocDTO::new).collect(Collectors.toList())
                : null;
        this.actif = magasin.isActif();
    }

    public static MagasinDTO fromEntity(Magasin magasin) {
        return new MagasinDTO(magasin);
    }

    public static List<MagasinDTO> fromEntityList(List<Magasin> magasins) {
        return magasins.stream()
                .map(MagasinDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
