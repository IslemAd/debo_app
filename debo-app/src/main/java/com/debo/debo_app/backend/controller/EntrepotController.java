package com.debo.debo_app.backend.controller;

import com.debo.debo_app.backend.entity.Entrepot;
import com.debo.debo_app.backend.service.EntrepotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/entrepots")
@RequiredArgsConstructor
public class EntrepotController {

    private final EntrepotService entrepotService;

    @GetMapping
    public List<Entrepot> getAll(@RequestParam(required = false) String nom) {
        return (nom != null && !nom.isBlank())
                ? entrepotService.search(nom)
                : entrepotService.getAll();
    }

    @GetMapping("/{id}")
    public Entrepot getById(@PathVariable Long id) {
        return entrepotService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Entrepot> create(@Valid @RequestBody Entrepot entrepot) {
        return ResponseEntity.status(201).body(entrepotService.create(entrepot));
    }

    @PutMapping("/{id}")
    public Entrepot update(@PathVariable Long id, @Valid @RequestBody Entrepot entrepot) {
        return entrepotService.update(id, entrepot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entrepotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}