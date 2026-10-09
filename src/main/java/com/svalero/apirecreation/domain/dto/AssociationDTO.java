package com.svalero.apirecreation.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssociationDTO {

    @NotBlank(message = "El CIF/NIF de la asociación es obligatorio")
    private String taxId;

    @NotBlank(message = "El nombre de la asociación es obligatorio")
    private String name;

    // Campos opcionales (no necesitan validación estricta)
    private Integer foundationYear;
    private String city;
    private String allegiance;
    private String historicalAttire;
    private String logoBase64;
}
