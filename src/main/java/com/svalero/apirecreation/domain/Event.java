package com.svalero.apirecreation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// --- CONFIGURACIÓN PARA QUE EL JSON SEPA QUÉ TIPO DE EVENTO ES ---
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PublicEvent.class, name = "PUBLIC"),
        @JsonSubTypes.Type(value = PrivateEvent.class, name = "PRIVATE")
})
// -----------------------------------------------------------------

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de tablas unidas
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_code", nullable = false, unique = true, length = 50)
    private String eventCode;

    // Relación N:1 -> Muchos eventos son organizados por 1 Asociación
    @ManyToOne
    @JoinColumn(name = "organizing_association_id", nullable = false)
    private Association organizingAssociation;

    @Column(length = 50)
    private String country;

    @Column(length = 50)
    private String city;

    @Column(name = "reenactors_count")
    private Integer reenactorsCount = 0;

    @Column(name = "visitors_count")
    private Integer visitorsCount = 0;

    private BigDecimal cost;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private Boolean published = false; // Control para el Admin

    // --- RELACIONES BIDIRECCIONALES ---

    // Asociaciones (invitadas) que asisten a este evento
    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventAttendance> attendances;

    // Recreadores inscritos en este evento
    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipation> participations;
}