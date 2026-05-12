package com.debo.debo_app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "mouvement_stock")
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"produit", "entrepot", "utilisateur"})
@EqualsAndHashCode(of = "id")
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement type;

    @Min(value = 1, message = "La quantité doit être >= 1")
    private int quantite;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false)
    @JsonIgnoreProperties({"stocks", "mouvements"})
    private Produit produit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrepot_id", nullable = false)
    @JsonIgnoreProperties({"stocks", "mouvements"})
    private Entrepot entrepot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    @JsonIgnoreProperties({"motDePasse"})
    private Utilisateur utilisateur;
}