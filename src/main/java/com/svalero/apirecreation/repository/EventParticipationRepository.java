package com.svalero.apirecreation.repository;

import com.svalero.apirecreation.domain.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {

    // Para que el Admin o Presidente vea quiénes están apuntados a un evento concreto
    List<EventParticipation> findByEventId(Long eventId);

    // Para que un Member (Recreador) vea su propio historial de eventos
    List<EventParticipation> findByMemberId(Long memberId);

    // Validar si un miembro ya está inscrito en un evento para no duplicar inscripciones
    boolean existsByEventIdAndMemberId(Long eventId, Long memberId);
}
