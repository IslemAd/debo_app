package com.debo.debo_app.backend.controller;

import com.debo.debo_app.backend.entity.Alerte;
import com.debo.debo_app.backend.service.AlerteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alertes")
@RequiredArgsConstructor
public class AlerteController {

    private final AlerteService alerteService;

    @GetMapping
    public List<Alerte> getAll() { return alerteService.getAll(); }

    @GetMapping("/non-resolues")
    public List<Alerte> getNonResolues() { return alerteService.getNonResolues(); }

    @PatchMapping("/{id}/resoudre")
    public Alerte resoudre(@PathVariable Long id) {
        return alerteService.resoudre(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alerteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}