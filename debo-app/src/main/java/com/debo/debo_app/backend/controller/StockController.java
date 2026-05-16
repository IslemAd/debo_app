package com.debo.debo_app.backend.controller;

import com.debo.debo_app.backend.dto.StockRequest;
import com.debo.debo_app.backend.entity.Stock;
import com.debo.debo_app.backend.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<Stock> getAll() { return stockService.getAll(); }

    @GetMapping("/{id}")
    public Stock getById(@PathVariable Long id) { return stockService.getById(id); }

    @GetMapping("/entrepot/{entrepotId}")
    public List<Stock> getByEntrepot(@PathVariable Long entrepotId) {
        return stockService.getByEntrepot(entrepotId);
    }

    @GetMapping("/produit/{produitId}")
    public List<Stock> getByProduit(@PathVariable Long produitId) {
        return stockService.getByProduit(produitId);
    }

    @GetMapping("/alertes")
    public List<Stock> getStocksEnAlerte() {
        return stockService.getStocksEnAlerte();
    }

    @PostMapping
    public ResponseEntity<Stock> create(@Valid @RequestBody StockRequest req) {
        return ResponseEntity.status(201).body(stockService.create(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
public ResponseEntity<Stock> update(@PathVariable Long id,
                                    @Valid @RequestBody StockRequest req) {
    return ResponseEntity.ok(stockService.update(id, req));
}
}