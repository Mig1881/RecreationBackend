package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.EventParticipation;
import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.domain.dto.EventParticipationDTO;
import com.svalero.apirecreation.exception.DuplicateEnrollmentException;
import com.svalero.apirecreation.repository.EventParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventParticipationService {

    @Autowired
    private EventParticipationRepository participationRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private MemberService memberService;

    public List<EventParticipation> findAll() {
        return participationRepository.findAll();
    }

    public List<EventParticipation> getParticipationsByEvent(Long eventId) {
        return participationRepository.findByEventId(eventId);
    }

    public List<EventParticipation> getParticipationsByMember(Long memberId) {
        return participationRepository.findByMemberId(memberId);
    }

    public EventParticipation enrollMember(EventParticipationDTO dto) {

        // 1. Validar que no esté inscrito ya
        if (participationRepository.existsByEventIdAndMemberId(dto.getEventId(), dto.getMemberId())) {
            throw new DuplicateEnrollmentException("El miembro ya está inscrito en este evento.");
        }

        // 2. Buscar las entidades reales en la base de datos
        Event event = eventService.findById(dto.getEventId());
        Member member = memberService.findById(dto.getMemberId());

        // 3. Crear la inscripción limpia, enlazarlas y guardar
        EventParticipation participation = new EventParticipation();
        participation.setEvent(event);
        participation.setMember(member);
        participation.setCarriedWeapon(dto.getCarriedWeapon());
        participation.setWeaponModel(dto.getWeaponModel());

        return participationRepository.save(participation);
    }

    // Aquí irían también los métodos update y delete, siguiendo la misma estructura que en otras entidades
    public void delete(Long id) {
        participationRepository.deleteById(id);
    }
}
