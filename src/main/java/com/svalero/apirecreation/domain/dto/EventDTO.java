package com.svalero.apirecreation.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EventDTO {

    // --- CAMPOS COMUNES OBLIGATORIOS ---
    @NotBlank(message = "El código del evento es obligatorio")
    private String eventCode;

    //
    @NotNull(message = "La asociación organizadora es obligatoria")
    @Valid // Le decimos a Spring que también valide lo que hay dentro de este objeto
    private AssociationReference organizingAssociation;

    @NotBlank(message = "El tipo de evento (PUBLIC o PRIVATE) es obligatorio")
    private String eventType;

    // --- CAMPOS COMUNES OPCIONALES ---
    private String country;
    private String city;
    private BigDecimal cost;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean published;
    private Integer reenactorsCount;
    private Integer visitorsCount;

    // --- CAMPOS ESPECÍFICOS DE PUBLIC EVENT ---
    private String publicEntity;
    private BigDecimal subsidy;

    // --- CAMPOS ESPECÍFICOS DE PRIVATE EVENT ---
    private String sponsor;
    private BigDecimal initialBudget;
    private Boolean openToPublic;

    // --- CLASE ANIDADA PARA MAPEAR EL JSON DEL FRONTEND ---
    @Data
    public static class AssociationReference {
        @NotNull(message = "El ID de la asociación organizadora es obligatorio")
        private Long id;
    }
}
