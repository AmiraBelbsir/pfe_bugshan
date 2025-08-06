package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@CrossOrigin(origins = "*")  // Autoriser le front Angular à appeler ce backend
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @GetMapping
    public List<RendezVous> getAll() {
        return rendezVousService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getById(@PathVariable Long id) {
        return rendezVousService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RendezVous create(@RequestBody RendezVous rdv) {
        return rendezVousService.save(rdv);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> update(@PathVariable Long id, @RequestBody RendezVous rdvDetails) {
        try {
            RendezVous updated = rendezVousService.update(id, rdvDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rendezVousService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
