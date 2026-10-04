package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.PrivateEvent;
import com.svalero.apirecreation.domain.PublicEvent;
import com.svalero.apirecreation.domain.dto.EventOutDto;
import com.svalero.apirecreation.exception.EventNotFoundException;
import com.svalero.apirecreation.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AssociationService associationService;

    // 1. Convertimos la lista de entidades a DTOs de salida
    public List<EventOutDto> findAll() {
        return eventRepository.findAll().stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    // 2. Convertimos la lista de eventos publicados a DTOs
    public List<EventOutDto> findPublishedEvents() {
        return eventRepository.findByPublishedTrue().stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    // 3. ¡CRÍTICO!: Mantenemos este método devolviendo la Entidad 'Event'.
    // Los servicios de Participaciones y Asistencias necesitan la Entidad real para guardar en BBDD.
    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con ID: " + id));
    }

    // 4. NUEVO: Método específico para que el Controlador pida un solo evento en formato DTO
    public EventOutDto findDtoById(Long id) {
        Event event = findById(id);
        return mapToOutDto(event);
    }

    // 5. Guardamos y mapeamos el resultado a DTO
    public EventOutDto save(Event event) {
        if (event.getOrganizingAssociation() != null && event.getOrganizingAssociation().getId() != null) {
            Long associationId = event.getOrganizingAssociation().getId();
            Association organizingAssociation = associationService.findById(associationId);
            event.setOrganizingAssociation(organizingAssociation);
        } else {
            event.setOrganizingAssociation(null);
        }

        Event savedEvent = eventRepository.save(event);
        log.info("Nuevo evento creado: {}", savedEvent.getEventCode());
        return mapToOutDto(savedEvent);
    }

    // 6. Actualizamos y mapeamos el resultado a DTO
    public EventOutDto update(Long id, Event eventDetails) {
        Event existingEvent = findById(id);

        existingEvent.setEventCode(eventDetails.getEventCode());
        existingEvent.setCountry(eventDetails.getCountry());
        existingEvent.setCity(eventDetails.getCity());
        existingEvent.setCost(eventDetails.getCost());
        existingEvent.setStartDate(eventDetails.getStartDate());
        existingEvent.setEndDate(eventDetails.getEndDate());
        existingEvent.setPublished(eventDetails.getPublished());
        existingEvent.setReenactorsCount(eventDetails.getReenactorsCount());
        existingEvent.setVisitorsCount(eventDetails.getVisitorsCount());

        Long newAssociationId = eventDetails.getOrganizingAssociation().getId();
        if (!existingEvent.getOrganizingAssociation().getId().equals(newAssociationId)) {
            Association newAssociation = associationService.findById(newAssociationId);
            existingEvent.setOrganizingAssociation(newAssociation);
        }

        Event updatedEvent = eventRepository.save(existingEvent);
        log.info("Evento actualizado: {}", updatedEvent.getEventCode());
        return mapToOutDto(updatedEvent);
    }

    public void delete(Long id) {
        Event existingEvent = findById(id);
        eventRepository.delete(existingEvent);
        log.info("Evento eliminado con ID: {}", id);
    }

    // --- MÉTODO PRIVADO DE MAPEO A DTO ---
    private EventOutDto mapToOutDto(Event event) {
        EventOutDto dto = new EventOutDto();
        dto.setId(event.getId());
        dto.setEventCode(event.getEventCode());

        // Comprobación de seguridad por si el evento no tiene asociación organizadora
        if (event.getOrganizingAssociation() != null) {
            dto.setOrganizingAssociationId(event.getOrganizingAssociation().getId());
            dto.setOrganizingAssociationName(event.getOrganizingAssociation().getName());
        }

        dto.setCountry(event.getCountry());
        dto.setCity(event.getCity());
        dto.setReenactorsCount(event.getReenactorsCount());
        dto.setVisitorsCount(event.getVisitorsCount());
        dto.setCost(event.getCost());
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        dto.setPublished(event.getPublished());

        // Extraer los IDs de las asociaciones invitadas (Lo que pedía el frontend)
        List<Long> attendingIds = event.getAttendances() == null ? List.of() :
                event.getAttendances().stream()
                        .map(attendance -> attendance.getAssociation().getId())
                        .collect(Collectors.toList());
        dto.setAttendingAssociationIds(attendingIds);

        // Comprobación polimórfica para los campos específicos de herencia
        if (event instanceof PublicEvent) {
            dto.setEventType("PUBLIC");
            dto.setPublicEntity(((PublicEvent) event).getPublicEntity());
            dto.setSubsidy(((PublicEvent) event).getSubsidy());
        } else if (event instanceof PrivateEvent) {
            dto.setEventType("PRIVATE");
            dto.setSponsor(((PrivateEvent) event).getSponsor());
            dto.setInitialBudget(((PrivateEvent) event).getInitialBudget());
            dto.setOpenToPublic(((PrivateEvent) event).getOpenToPublic());
        }

        return dto;
    }
}