package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.EventParticipation;
import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.domain.dto.EventParticipationDTO;
import com.svalero.apirecreation.domain.dto.EventParticipationOutDto;
import com.svalero.apirecreation.exception.DuplicateEnrollmentException;
import com.svalero.apirecreation.exception.EventParticipationNotFoundException;
import com.svalero.apirecreation.repository.EventParticipationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventParticipationService {

    @Autowired
    private EventParticipationRepository participationRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private MemberService memberService;

    // Convertimos la lista completa de Entidades a lista de DTOs
    public List<EventParticipationOutDto> findAll() {
        return participationRepository.findAll().stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    //Convertimos la lista filtrada por evento a DTOs
    public List<EventParticipationOutDto> getParticipationsByEvent(Long eventId) {
        return participationRepository.findByEventId(eventId).stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    //Convertimos la lista filtrada por miembro a DTOs
    public List<EventParticipationOutDto> getParticipationsByMember(Long memberId) {
        return participationRepository.findByMemberId(memberId).stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    //Inscribimos y devolvemos solo la información limpia (DTO)
    public EventParticipationOutDto enrollMember(EventParticipationDTO dto) {

        // Validar que no esté inscrito ya
        if (participationRepository.existsByEventIdAndMemberId(dto.getEventId(), dto.getMemberId())) {
            log.warn("Intento de inscripción duplicada: Evento {} - Miembro {}", dto.getEventId(), dto.getMemberId());
            throw new DuplicateEnrollmentException("El miembro ya está inscrito en este evento.");
        }

        // Buscar las entidades reales en la base de datos
        Event event = eventService.findById(dto.getEventId());
        Member member = memberService.findById(dto.getMemberId());

        // Crear la inscripción limpia, enlazarlas y guardar
        EventParticipation participation = new EventParticipation();
        participation.setEvent(event);
        participation.setMember(member);
        participation.setCarriedWeapon(dto.getCarriedWeapon());
        participation.setWeaponModel(dto.getWeaponModel());

        EventParticipation savedParticipation = participationRepository.save(participation);
        log.info("Nueva inscripción registrada: Miembro {} en Evento {}", member.getId(), event.getEventCode());

        // Mapear la entidad guardada al DTO de salida
        return mapToOutDto(savedParticipation);
    }

    //Borrado controlado (siempre es mejor comprobar si existe antes de borrar)
    public void delete(Long id) {
        EventParticipation participation = participationRepository.findById(id)
                .orElseThrow(() -> new EventParticipationNotFoundException("Inscripción no encontrada con ID: " + id));

        participationRepository.delete(participation);
        log.info("Inscripción eliminada con ID: {}", id);
    }


    // Método que actúa como traductor de la base de datos a la red
    private EventParticipationOutDto mapToOutDto(EventParticipation participation) {
        return new EventParticipationOutDto(
                participation.getId(),
                participation.getEvent().getId(),
                participation.getEvent().getEventCode(),
                participation.getMember().getId(),
                participation.getMember().getFirstName() + " " + participation.getMember().getLastName(),
                participation.getCarriedWeapon(),
                participation.getWeaponModel()
        );
    }
}