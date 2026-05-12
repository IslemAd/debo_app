package com.debo.debo_app.backend.repository;

import com.debo.debo_app.backend.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCategorie(String categorie);
    List<Produit> findByNomContainingIgnoreCase(String nom);
    List<Produit> findByFournisseur(String fournisseur);
}