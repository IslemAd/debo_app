package com.debo.debo_app.backend.repository;

import com.debo.debo_app.backend.entity.Entrepot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntrepotRepository extends JpaRepository<Entrepot, Long> {
    List<Entrepot> findByNomContainingIgnoreCase(String nom);
    boolean existsByNom(String nom);
    boolean existsByNomAndIdNot(String nom, Long id);
}