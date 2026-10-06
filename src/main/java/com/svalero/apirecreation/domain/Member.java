package com.svalero.apirecreation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "national_id", nullable = false, unique = true, length = 20)
    private String nationalId;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(length = 20)
    private String phone;

    @Column(name = "association_position", length = 50)
    private String associationPosition;

    @Column(name = "historical_rank", length = 50)
    private String historicalRank;

    @Column(name = "weapon_license", length = 50)
    private String weaponLicense;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "image_url")
    private String imageUrl; // Aquí guardaremos la URL que nos devuelva Cloudinary

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password; // Se almacenará encriptada con BCrypt

    @Column(nullable = false, length = 20)
    private String role; // Valores esperados: "admin", "president", "member"

    // --- RELACIONES BIDIRECCIONALES ---

    // Asociaciones a las que pertenece este miembro
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships;

    // Eventos en los que participa este miembro
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipation> eventParticipations;
}