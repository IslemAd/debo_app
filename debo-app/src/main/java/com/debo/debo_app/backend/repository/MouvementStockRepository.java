package com.debo.debo_app.backend.repository;

import com.debo.debo_app.backend.entity.MouvementStock;
import com.debo.debo_app.backend.entity.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
    List<MouvementStock> findByProduitId(Long produitId);
    List<MouvementStock> findByEntrepotId(Long entrepotId);
    List<MouvementStock> findByType(TypeMouvement type);
    List<MouvementStock> findByDateBetween(LocalDate debut, LocalDate fin);
    List<MouvementStock> findByUtilisateurId(Long utilisateurId);
}