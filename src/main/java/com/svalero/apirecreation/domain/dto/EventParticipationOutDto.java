package com.svalero.apirecreation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipationOutDto {
    private Long id;

    // Datos planos del Evento (lo justo y necesario)
    private Long eventId;
    private String eventCode;

    // Datos planos del Miembro (sin contraseñas, ni emails sensibles)
    private Long memberId;
    private String memberFullName;

    // Datos propios de la participación
    private String carriedWeapon;
    private String weaponModel;
    private String qrToken;
}
