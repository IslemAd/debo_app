package com.debo.debo_app.backend.repository;

import com.debo.debo_app.backend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByEntrepotId(Long entrepotId);
    List<Stock> findByProduitId(Long produitId);
    Optional<Stock> findByProduitIdAndEntrepotId(Long produitId, Long entrepotId);
    boolean existsByProduitIdAndEntrepotId(Long produitId, Long entrepotId);

    @Query("SELECT s FROM Stock s WHERE s.quantite <= s.seuilAlerte")
    List<Stock> findStocksEnAlerte();
}