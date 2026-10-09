package com.svalero.apirecreation.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MembershipDTO {

    @NotNull(message = "El ID de la asociación es obligatorio")
    private Long associationId;

    @NotNull(message = "El ID del miembro es obligatorio")
    private Long memberId;
}
