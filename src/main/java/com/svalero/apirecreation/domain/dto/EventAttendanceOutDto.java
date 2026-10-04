package com.svalero.apirecreation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventAttendanceOutDto {
    private Long id;

    // Datos del Evento
    private Long eventId;
    private String eventCode;

    // Datos de la Asociación invitada
    private Long associationId;
    private String associationName;
}
