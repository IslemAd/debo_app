package com.debo.debo_app.backend.service;

import com.debo.debo_app.backend.entity.Alerte;
import com.debo.debo_app.backend.repository.AlerteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlerteService {

    private final AlerteRepository alerteRepository;

    @Transactional(readOnly = true)
    public List<Alerte> getAll() {
        return alerteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Alerte> getNonResolues() {
        return alerteRepository.findByResolue(false);
    }

    @Transactional
    public Alerte resoudre(Long id) {
        Alerte alerte = alerteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerte non trouvée : " + id));
        if (alerte.isResolue())
            throw new IllegalStateException("Cette alerte est déjà résolue");
        alerte.setResolue(true);
        return alerteRepository.save(alerte);
    }

    @Transactional
    public void delete(Long id) {
        if (!alerteRepository.existsById(id))
            throw new EntityNotFoundException("Alerte non trouvée : " + id);
        alerteRepository.deleteById(id);
    }
}