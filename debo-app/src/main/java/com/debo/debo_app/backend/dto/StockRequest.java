package com.debo.debo_app.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockRequest {

    @NotNull(message = "Le produit est obligatoire")
    private Long produitId;

    @NotNull(message = "L'entrepôt est obligatoire")
    private Long entrepotId;

    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private int quantite;

    @Min(value = 0, message = "Le seuil d'alerte doit être >= 0")
    private int seuilAlerte;
}