package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.EventAttendance;
import com.svalero.apirecreation.domain.dto.EventAttendanceDTO;
import com.svalero.apirecreation.domain.dto.EventAttendanceOutDto;
import com.svalero.apirecreation.exception.DuplicateEnrollmentException;
import com.svalero.apirecreation.exception.EventAttendanceNotFoundException;
import com.svalero.apirecreation.repository.EventAttendanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventAttendanceService {

    @Autowired
    private EventAttendanceRepository attendanceRepository;

    @Autowired
    private AssociationService associationService;

    @Autowired
    private EventService eventService;

    // 1. Convertimos la lista de entidades a DTOs de salida
    public List<EventAttendanceOutDto> findAll() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    // 2. Registramos y devolvemos el DTO
    public EventAttendanceOutDto registerAttendance(EventAttendanceDTO dto) {
        if (attendanceRepository.existsByAssociationIdAndEventId(dto.getAssociationId(), dto.getEventId())) {
            log.warn("Intento de asistencia duplicada: Asociación {} - Evento {}", dto.getAssociationId(), dto.getEventId());
            throw new DuplicateEnrollmentException("La asociación ya está registrada como asistente a este evento.");
        }

        Association association = associationService.findById(dto.getAssociationId());
        Event event = eventService.findById(dto.getEventId());

        EventAttendance attendance = new EventAttendance();
        attendance.setAssociation(association);
        attendance.setEvent(event);

        EventAttendance savedAttendance = attendanceRepository.save(attendance);
        log.info("Nueva asistencia registrada: Asociación {} en Evento {}", association.getName(), event.getEventCode());

        return mapToOutDto(savedAttendance);
    }

    // 3. Borrado controlado (comprobando existencia primero)
    public void delete(Long id) {
        EventAttendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new EventAttendanceNotFoundException("Asistencia no encontrada con ID: " + id));

        attendanceRepository.delete(attendance);
        log.info("Asistencia de evento eliminada con ID: {}", id);
    }

    // --- MÉTODO PRIVADO DE MAPEO ---
    private EventAttendanceOutDto mapToOutDto(EventAttendance attendance) {
        return new EventAttendanceOutDto(
                attendance.getId(),
                attendance.getEvent().getId(),
                attendance.getEvent().getEventCode(),
                attendance.getAssociation().getId(),
                attendance.getAssociation().getName()
        );
    }
}
