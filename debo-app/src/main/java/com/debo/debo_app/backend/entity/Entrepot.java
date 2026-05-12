package com.debo.debo_app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrepot")
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"stocks", "mouvements"})
@EqualsAndHashCode(of = "id")
public class Entrepot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100)
    @Column(length = 100, nullable = false, unique = true)
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(length = 200, nullable = false)
    private String adresse;

    @Min(value = 1, message = "La capacité doit être >= 1")
    private int capacite;

    @JsonIgnore
    @OneToMany(mappedBy = "entrepot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stock> stocks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "entrepot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MouvementStock> mouvements = new ArrayList<>();
}