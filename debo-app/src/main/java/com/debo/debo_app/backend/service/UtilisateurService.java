package com.debo.debo_app.backend.service;

import com.debo.debo_app.backend.dto.UtilisateurRequest;
import com.debo.debo_app.backend.entity.Utilisateur;
import com.debo.debo_app.backend.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Transactional(readOnly = true)
    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Utilisateur getById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé : " + id));
    }

    @Transactional
    public Utilisateur create(UtilisateurRequest req) {
        if (utilisateurRepository.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Cet email est déjà utilisé");

        Utilisateur u = new Utilisateur();
        u.setNom(req.getNom());
        u.setEmail(req.getEmail());
        u.setMotDePasse(req.getMotDePasse());
        u.setRole(req.getRole());
        return utilisateurRepository.save(u);
    }

    @Transactional
    public Utilisateur update(Long id, UtilisateurRequest req) {
        Utilisateur existing = getById(id);
        if (utilisateurRepository.existsByEmailAndIdNot(req.getEmail(), id))
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        existing.setNom(req.getNom());
        existing.setEmail(req.getEmail());
        existing.setRole(req.getRole());
        if (req.getMotDePasse() != null && !req.getMotDePasse().isBlank())
            existing.setMotDePasse(req.getMotDePasse());
        return utilisateurRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!utilisateurRepository.existsById(id))
            throw new EntityNotFoundException("Utilisateur non trouvé : " + id);
        utilisateurRepository.deleteById(id);
    }
}