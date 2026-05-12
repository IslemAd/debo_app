package com.debo.debo_app.backend.dto;

import com.debo.debo_app.backend.entity.TypeMouvement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MouvementRequest {

    @NotNull(message = "Le produit est obligatoire")
    private Long produitId;

    @NotNull(message = "L'entrepôt est obligatoire")
    private Long entrepotId;

    @NotNull(message = "Le type est obligatoire (ENTREE ou SORTIE)")
    private TypeMouvement type;

    @Min(value = 1, message = "La quantité doit être >= 1")
    private int quantite;

    private Long utilisateurId; // optionnel
}