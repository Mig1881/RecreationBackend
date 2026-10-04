package com.svalero.apirecreation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
