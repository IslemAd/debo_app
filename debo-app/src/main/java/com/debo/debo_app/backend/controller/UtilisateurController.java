package com.debo.debo_app.backend.controller;

import com.debo.debo_app.backend.dto.UtilisateurRequest;
import com.debo.debo_app.backend.entity.Utilisateur;
import com.debo.debo_app.backend.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public List<Utilisateur> getAll() { return utilisateurService.getAll(); }

    @GetMapping("/{id}")
    public Utilisateur getById(@PathVariable Long id) {
        return utilisateurService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Utilisateur> create(@Valid @RequestBody UtilisateurRequest req) {
        return ResponseEntity.status(201).body(utilisateurService.create(req));
    }

    @PutMapping("/{id}")
    public Utilisateur update(@PathVariable Long id, @Valid @RequestBody UtilisateurRequest req) {
        return utilisateurService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}