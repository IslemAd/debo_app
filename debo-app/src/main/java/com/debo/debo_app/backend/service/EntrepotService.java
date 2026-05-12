package com.debo.debo_app.backend.service;

import com.debo.debo_app.backend.entity.Entrepot;
import com.debo.debo_app.backend.repository.EntrepotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntrepotService {

    private final EntrepotRepository entrepotRepository;

    @Transactional(readOnly = true)
    public List<Entrepot> getAll() {
        return entrepotRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Entrepot getById(Long id) {
        return entrepotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entrepôt non trouvé : " + id));
    }

    @Transactional
    public Entrepot create(Entrepot entrepot) {
        if (entrepotRepository.existsByNom(entrepot.getNom()))
            throw new IllegalArgumentException("Un entrepôt avec ce nom existe déjà");
        return entrepotRepository.save(entrepot);
    }

    @Transactional
    public Entrepot update(Long id, Entrepot data) {
        Entrepot existing = getById(id);
        if (entrepotRepository.existsByNomAndIdNot(data.getNom(), id))
            throw new IllegalArgumentException("Un entrepôt avec ce nom existe déjà");
        existing.setNom(data.getNom());
        existing.setAdresse(data.getAdresse());
        existing.setCapacite(data.getCapacite());
        return entrepotRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!entrepotRepository.existsById(id))
            throw new EntityNotFoundException("Entrepôt non trouvé : " + id);
        entrepotRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Entrepot> search(String nom) {
        return entrepotRepository.findByNomContainingIgnoreCase(nom);
    }
}