package com.svalero.apirecreation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
