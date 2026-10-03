package com.svalero.apirecreation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "associations")
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_id", nullable = false, unique = true, length = 20)
    private String taxId;

    @Column(name = "foundation_year")
    private Integer foundationYear;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String allegiance;

    @Column(name = "historical_attire", columnDefinition = "TEXT")
    private String historicalAttire;

    @Column(name = "logo_base64", columnDefinition = "LONGTEXT")
    private String logoBase64;

    // --- RELACIONES BIDIRECCIONALES ---

    // Miembros que pertenecen a esta asociación
    @JsonIgnore
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships;

    // Eventos que esta asociación organiza
    @JsonIgnore
    @OneToMany(mappedBy = "organizingAssociation", cascade = CascadeType.ALL)
    private List<Event> organizedEvents;

    // Eventos a los que esta asociación asiste como invitada
    @JsonIgnore
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventAttendance> eventAttendances;
}