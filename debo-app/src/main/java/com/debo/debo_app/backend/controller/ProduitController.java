package com.debo.debo_app.backend.controller;

import com.debo.debo_app.backend.entity.Produit;
import com.debo.debo_app.backend.service.ProduitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    public List<Produit> getAll(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String categorie) {
        if (nom != null && !nom.isBlank()) return produitService.search(nom);
        if (categorie != null && !categorie.isBlank()) return produitService.getByCategorie(categorie);
        return produitService.getAll();
    }

    @GetMapping("/{id}")
    public Produit getById(@PathVariable Long id) {
        return produitService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Produit> create(@Valid @RequestBody Produit produit) {
        return ResponseEntity.status(201).body(produitService.create(produit));
    }

    @PutMapping("/{id}")
    public Produit update(@PathVariable Long id, @Valid @RequestBody Produit produit) {
        return produitService.update(id, produit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}