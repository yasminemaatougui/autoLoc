package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    @Column(nullable = false, unique = true, length = 30)
    private String immatriculation;

    @Column(nullable = false, length = 50)
    private String marque;

    @Column(nullable = false, length = 50)
    private String modele;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategorieVehicule categorie;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutVehicule statut;

    @JsonBackReference("agence-vehicules")
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_agence", nullable = true)
    private Agence agence;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "vehicule_equipement",
            joinColumns = @JoinColumn(name = "id_vehicule"),
            inverseJoinColumns = @JoinColumn(name = "id_equipement"))
    private List<Equipement> equipements = new ArrayList<>();
}
