package com.debo.debo_app.backend.service;

import com.debo.debo_app.backend.dto.MouvementRequest;
import com.debo.debo_app.backend.entity.*;
import com.debo.debo_app.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MouvementStockService {

    private final MouvementStockRepository mouvementRepository;
    private final StockRepository stockRepository;
    private final ProduitRepository produitRepository;
    private final EntrepotRepository entrepotRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AlerteRepository alerteRepository;

    @Transactional
    public MouvementStock enregistrer(MouvementRequest req) {
        Produit produit = produitRepository.findById(req.getProduitId())
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé : " + req.getProduitId()));
        Entrepot entrepot = entrepotRepository.findById(req.getEntrepotId())
                .orElseThrow(() -> new EntityNotFoundException("Entrepôt non trouvé : " + req.getEntrepotId()));

        // Trouver ou créer le stock automatiquement
        Stock stock = stockRepository
                .findByProduitIdAndEntrepotId(req.getProduitId(), req.getEntrepotId())
                .orElseGet(() -> {
                    Stock s = new Stock();
                    s.setProduit(produit);
                    s.setEntrepot(entrepot);
                    s.setQuantite(0);
                    s.setSeuilAlerte(produit.getSeuilMin());
                    return stockRepository.save(s);
                });

        // Mettre à jour la quantité
        if (req.getType() == TypeMouvement.ENTREE) {
            stock.setQuantite(stock.getQuantite() + req.getQuantite());
        } else {
            if (stock.getQuantite() < req.getQuantite())
                throw new IllegalStateException(
                        "Stock insuffisant. Disponible : " + stock.getQuantite()
                                + ", demandé : " + req.getQuantite());
            stock.setQuantite(stock.getQuantite() - req.getQuantite());
        }
        stockRepository.save(stock);

        // Créer une alerte si seuil dépassé (après SORTIE uniquement)
        if (req.getType() == TypeMouvement.SORTIE
                && stock.getQuantite() <= stock.getSeuilAlerte()) {
            Alerte alerte = new Alerte();
            alerte.setStock(stock);
            alerte.setMessage("Stock bas pour « " + produit.getNom()
                    + " » dans « " + entrepot.getNom()
                    + " » : " + stock.getQuantite() + " unité(s) restante(s)");
            alerte.setDateCreation(LocalDate.now());
            alerte.setResolue(false);
            alerteRepository.save(alerte);
        }

        // Créer le mouvement
        MouvementStock mouvement = new MouvementStock();
        mouvement.setProduit(produit);
        mouvement.setEntrepot(entrepot);
        mouvement.setType(req.getType());
        mouvement.setQuantite(req.getQuantite());
        mouvement.setDate(LocalDate.now());

        if (req.getUtilisateurId() != null) {
            utilisateurRepository.findById(req.getUtilisateurId())
                    .ifPresent(mouvement::setUtilisateur);
        }

        return mouvementRepository.save(mouvement);
    }

    @Transactional(readOnly = true)
    public List<MouvementStock> getAll() {
        return mouvementRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MouvementStock> getByProduit(Long produitId) {
        return mouvementRepository.findByProduitId(produitId);
    }

    @Transactional(readOnly = true)
    public List<MouvementStock> getByEntrepot(Long entrepotId) {
        return mouvementRepository.findByEntrepotId(entrepotId);
    }

    @Transactional(readOnly = true)
    public List<MouvementStock> getByPeriode(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin))
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        return mouvementRepository.findByDateBetween(debut, fin);
    }
}