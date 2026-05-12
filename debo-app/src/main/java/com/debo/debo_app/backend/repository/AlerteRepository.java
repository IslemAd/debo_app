package com.debo.debo_app.backend.repository;

import com.debo.debo_app.backend.entity.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    List<Alerte> findByResolue(boolean resolue);
    List<Alerte> findByStockId(Long stockId);
}