package com.svalero.apirecreation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventOutDto {
    private Long id;
    private String eventType; // "PUBLIC" o "PRIVATE"
    private String eventCode;

    // Datos planos de la asociación organizadora
    private Long organizingAssociationId;
    private String organizingAssociationName;

    // Campos comunes
    private String country;
    private String city;
    private Integer reenactorsCount;
    private Integer visitorsCount;
    private BigDecimal cost;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean published;

    // Campos específicos (pueden ir a null si no aplican)
    private String publicEntity;
    private BigDecimal subsidy;
    private String sponsor;
    private BigDecimal initialBudget;
    private Boolean openToPublic;


    private List<Long> attendingAssociationIds;
}