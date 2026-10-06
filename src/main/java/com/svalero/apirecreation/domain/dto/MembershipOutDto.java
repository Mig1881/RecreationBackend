package com.svalero.apirecreation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipOutDto {
    private Long id;

    // Datos de la Asociación
    private Long associationId;
    private String associationName;

    // Datos del Miembro
    private Long memberId;
    private String memberFullName;

    //PARA LA TABLA DEL PRESIDENTE
    private String nationalId;
    private String historicalRank;
    private String weaponLicense;
    private String email;

    // Histórico
    private LocalDate startDate;
    private LocalDate endDate;
}
