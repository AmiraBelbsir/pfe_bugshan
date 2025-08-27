package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.AvisDTO;
import com.bugshan.automative.group.app.dto.BlocDTO;
import com.bugshan.automative.group.app.service.AvisService;
import com.bugshan.automative.group.app.service.BlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/avis")
@CrossOrigin(origins = "http://localhost:4200")
public class AvisController {

    @Autowired
    private AvisService avisService;

    @GetMapping
    public List<AvisDTO> getAllAvis() {
        return avisService.getAllAvis();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAvis(@RequestBody AvisDTO avisDTO) {
        try {
            AvisDTO createdAvis = avisService.createAvis(
                    avisDTO.getRdvId(),
                    avisDTO.getNote(),
                    avisDTO.getCommentaire(),
                    avisDTO.getUtilisateurId()   // récupéré depuis le corps JSON
            );
            return ResponseEntity.ok(createdAvis);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Erreur lors de la création de l'avis", "erreur", e.getMessage()));
        }
    }


}
