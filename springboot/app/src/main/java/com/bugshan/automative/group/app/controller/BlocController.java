package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.BlocDTO;
import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.service.BlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blocs")
@CrossOrigin(origins = "http://localhost:4200")// Autoriser Angular à appeler ce contrôleur
public class BlocController {

    @Autowired
    private BlocService blocService;

    @GetMapping
    public List<BlocDTO> getAllBlocs() {
        return blocService.getAllBlocs();
    }

    @GetMapping("/{id}")
    public BlocDTO getBlocById(@PathVariable Long id) {
        return blocService.getBlocById(id);
    }
    // URL : GET /api/blocs/magasin/{magasinId}
    @GetMapping("/magasin/{magasinId}")
    public List<BlocDTO> getBlocsByMagasinId(@PathVariable Long magasinId) {
        return blocService.getBlocsByMagasinId(magasinId); // ✅ retourne des DTO, pas d'entités
    }

}

