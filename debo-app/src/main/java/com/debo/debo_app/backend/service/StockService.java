package com.debo.debo_app.backend.service;

import com.debo.debo_app.backend.dto.StockRequest;
import com.debo.debo_app.backend.entity.*;
import com.debo.debo_app.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProduitRepository produitRepository;
    private final EntrepotRepository entrepotRepository;

    @Transactional(readOnly = true)
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Stock getById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock non trouvé : " + id));
    }

    @Transactional
    public Stock create(StockRequest req) {
        // Vérifier doublon produit+entrepôt
        if (stockRepository.existsByProduitIdAndEntrepotId(req.getProduitId(), req.getEntrepotId()))
            throw new IllegalArgumentException(
                    "Un stock existe déjà pour ce produit dans cet entrepôt");

        Produit produit = produitRepository.findById(req.getProduitId())
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé : " + req.getProduitId()));
        Entrepot entrepot = entrepotRepository.findById(req.getEntrepotId())
                .orElseThrow(() -> new EntityNotFoundException("Entrepôt non trouvé : " + req.getEntrepotId()));

        Stock stock = new Stock();
        stock.setProduit(produit);
        stock.setEntrepot(entrepot);
        stock.setQuantite(req.getQuantite());
        stock.setSeuilAlerte(req.getSeuilAlerte());
        return stockRepository.save(stock);
    }

    @Transactional
    public void delete(Long id) {
        if (!stockRepository.existsById(id))
            throw new EntityNotFoundException("Stock non trouvé : " + id);
        stockRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Stock> getByEntrepot(Long entrepotId) {
        return stockRepository.findByEntrepotId(entrepotId);
    }

    @Transactional(readOnly = true)
    public List<Stock> getByProduit(Long produitId) {
        return stockRepository.findByProduitId(produitId);
    }

    @Transactional(readOnly = true)
    public List<Stock> getStocksEnAlerte() {
        return stockRepository.findStocksEnAlerte();
    }
}