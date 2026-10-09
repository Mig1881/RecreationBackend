package com.svalero.apirecreation.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventAttendanceDTO {

    @NotNull(message = "El ID de la asociación es obligatorio")
    private Long associationId;

    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventId;
}