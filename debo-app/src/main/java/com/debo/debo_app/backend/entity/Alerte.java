package com.debo.debo_app.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "alerte")
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"stock"})
@EqualsAndHashCode(of = "id")
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String message;

    @Column(nullable = false)
    private boolean resolue = false;

    private LocalDate dateCreation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", nullable = false)
    @JsonIgnoreProperties({"alertes"})
    private Stock stock;
}