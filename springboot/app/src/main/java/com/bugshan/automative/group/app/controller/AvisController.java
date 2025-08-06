package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.AvisDTO;
import com.bugshan.automative.group.app.dto.BlocDTO;
import com.bugshan.automative.group.app.service.AvisService;
import com.bugshan.automative.group.app.service.BlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avis")
@CrossOrigin(origins = "http://localhost:4200")// Autoriser Angular à appeler ce contrôleur
public class AvisController {

    @Autowired
    private AvisService avisService;

    @GetMapping
    public List<AvisDTO> getAllAvis() {
        return avisService.getAllAvis();
    }

}

