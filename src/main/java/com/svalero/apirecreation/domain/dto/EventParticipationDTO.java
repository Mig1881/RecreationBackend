package com.svalero.apirecreation.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventParticipationDTO {

    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventId;

    @NotNull(message = "El ID del miembro es obligatorio")
    private Long memberId;

    private String carriedWeapon;
    private String weaponModel;
}
