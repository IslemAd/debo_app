package com.debo.debo_app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produit")
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"stocks", "mouvements"})
@EqualsAndHashCode(of = "id")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100)
    @Column(length = 100, nullable = false)
    private String nom;

    @NotBlank(message = "La catégorie est obligatoire")
    @Column(length = 50, nullable = false)
    private String categorie;

    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être >= 0")
    private double prix;

    @Column(length = 100)
    private String fournisseur;

    @Min(value = 0, message = "Le seuil minimum doit être >= 0")
    private int seuilMin;

    @JsonIgnore
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stock> stocks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MouvementStock> mouvements = new ArrayList<>();
}