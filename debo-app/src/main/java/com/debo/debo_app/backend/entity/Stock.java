package com.debo.debo_app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "stock",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_stock_produit_entrepot",
                columnNames = {"produit_id", "entrepot_id"}
        )
)
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"alertes"})
@EqualsAndHashCode(of = "id")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private int quantite;

    @Min(value = 0, message = "Le seuil d'alerte doit être >= 0")
    private int seuilAlerte;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false)
    @JsonIgnoreProperties({"stocks", "mouvements"})
    private Produit produit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrepot_id", nullable = false)
    @JsonIgnoreProperties({"stocks", "mouvements"})
    private Entrepot entrepot;

    @JsonIgnore
    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alerte> alertes = new ArrayList<>();
}