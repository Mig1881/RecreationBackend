package com.svalero.apirecreation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_participations")
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el evento al que asiste
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Relación con el recreador que se inscribe
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "carried_weapon", length = 100)
    private String carriedWeapon;

    @Column(name = "weapon_model", length = 100)
    private String weaponModel;
}
