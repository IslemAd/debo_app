package com.debo.debo_app.backend.service;

import com.debo.debo_app.backend.entity.Produit;
import com.debo.debo_app.backend.repository.ProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    @Transactional(readOnly = true)
    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Produit getById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé : " + id));
    }

    @Transactional
    public Produit create(Produit produit) {
        return produitRepository.save(produit);
    }

    @Transactional
    public Produit update(Long id, Produit data) {
        Produit existing = getById(id);
        existing.setNom(data.getNom());
        existing.setCategorie(data.getCategorie());
        existing.setPrix(data.getPrix());
        existing.setFournisseur(data.getFournisseur());
        existing.setSeuilMin(data.getSeuilMin());
        return produitRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!produitRepository.existsById(id))
            throw new EntityNotFoundException("Produit non trouvé : " + id);
        produitRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Produit> getByCategorie(String categorie) {
        return produitRepository.findByCategorie(categorie);
    }

    @Transactional(readOnly = true)
    public List<Produit> search(String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom);
    }
}