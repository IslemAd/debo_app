package com.debo.debo_app.backend.controller;

import com.debo.debo_app.backend.dto.MouvementRequest;
import com.debo.debo_app.backend.entity.MouvementStock;
import com.debo.debo_app.backend.service.MouvementStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mouvements")
@RequiredArgsConstructor
public class MouvementStockController {

    private final MouvementStockService mouvementService;

    @GetMapping
    public List<MouvementStock> getAll() { return mouvementService.getAll(); }

    @GetMapping("/produit/{produitId}")
    public List<MouvementStock> getByProduit(@PathVariable Long produitId) {
        return mouvementService.getByProduit(produitId);
    }

    @GetMapping("/entrepot/{entrepotId}")
    public List<MouvementStock> getByEntrepot(@PathVariable Long entrepotId) {
        return mouvementService.getByEntrepot(entrepotId);
    }

    @GetMapping("/periode")
    public List<MouvementStock> getByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return mouvementService.getByPeriode(debut, fin);
    }

    @PostMapping
    public ResponseEntity<MouvementStock> enregistrer(@Valid @RequestBody MouvementRequest req) {
        return ResponseEntity.status(201).body(mouvementService.enregistrer(req));
    }
}