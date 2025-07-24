package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.Bloc;

public class BlocDTO {
    private Long id;
    private String nom;
    private Long magasinId;

    public BlocDTO() {}

    public BlocDTO(Long id, String nom, Long magasinId) {
        this.id = id;
        this.nom = nom;
        this.magasinId = magasinId;
    }

    public BlocDTO(Bloc bloc) {
        this.id = bloc.getId();
        this.nom = bloc.getNom();
        this.magasinId = bloc.getMagasin() != null ? bloc.getMagasin().getId() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Long getMagasinId() { return magasinId; }
    public void setMagasinId(Long magasinId) { this.magasinId = magasinId; }
}
