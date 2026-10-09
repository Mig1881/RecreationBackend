package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.PrivateEvent;
import com.svalero.apirecreation.domain.PublicEvent;
import com.svalero.apirecreation.domain.dto.EventDTO;
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

    public List<EventOutDto> findAll() {
        return eventRepository.findAll().stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    public List<EventOutDto> findPublishedEvents() {
        return eventRepository.findByPublishedTrue().stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con ID: " + id));
    }

    public EventOutDto findDtoById(Long id) {
        Event event = findById(id);
        return mapToOutDto(event);
    }

    // 🔥 Recibimos el EventDTO y fabricamos la clase correcta
    public EventOutDto save(EventDTO dto) {

        Event event;
        // Instanciamos la clase hija correspondiente según el DTO
        if ("PUBLIC".equalsIgnoreCase(dto.getEventType())) {
            PublicEvent publicEvent = new PublicEvent();
            publicEvent.setPublicEntity(dto.getPublicEntity());
            publicEvent.setSubsidy(dto.getSubsidy());
            event = publicEvent;
        } else if ("PRIVATE".equalsIgnoreCase(dto.getEventType())) {
            PrivateEvent privateEvent = new PrivateEvent();
            privateEvent.setSponsor(dto.getSponsor());
            privateEvent.setInitialBudget(dto.getInitialBudget());
            // Manejamos posibles nulos en booleanos
            privateEvent.setOpenToPublic(dto.getOpenToPublic() != null ? dto.getOpenToPublic() : false);
            event = privateEvent;
        } else {
            throw new IllegalArgumentException("Tipo de evento no válido. Debe ser PUBLIC o PRIVATE.");
        }

        // Mapeo de atributos comunes
        event.setEventCode(dto.getEventCode());
        event.setCountry(dto.getCountry());
        event.setCity(dto.getCity());
        event.setCost(dto.getCost());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
        event.setReenactorsCount(dto.getReenactorsCount() != null ? dto.getReenactorsCount() : 0);
        event.setVisitorsCount(dto.getVisitorsCount() != null ? dto.getVisitorsCount() : 0);

        // 🔥 CORRECCIÓN: Extraemos el ID navegando por el sub-objeto
        Association organizingAssociation = associationService.findById(dto.getOrganizingAssociation().getId());
        event.setOrganizingAssociation(organizingAssociation);

        Event savedEvent = eventRepository.save(event);
        log.info("Nuevo evento creado: {}", savedEvent.getEventCode());
        return mapToOutDto(savedEvent);
    }

    // 🔥 Recibimos el EventDTO y actualizamos
    public EventOutDto update(Long id, EventDTO dto) {
        Event existingEvent = findById(id);

        // 1. Validación de seguridad: No permitir cambiar el tipo de evento
        boolean isExistingPublic = existingEvent instanceof PublicEvent;
        boolean isIncomingPublic = "PUBLIC".equalsIgnoreCase(dto.getEventType());

        if (isExistingPublic != isIncomingPublic) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de un evento una vez creado.");
        }

        // 2. Mapeo de atributos comunes
        existingEvent.setEventCode(dto.getEventCode());
        existingEvent.setCountry(dto.getCountry());
        existingEvent.setCity(dto.getCity());
        existingEvent.setCost(dto.getCost());
        existingEvent.setStartDate(dto.getStartDate());
        existingEvent.setEndDate(dto.getEndDate());
        existingEvent.setPublished(dto.getPublished() != null ? dto.getPublished() : existingEvent.getPublished());

        // Si no vienen recuentos en el DTO, mantenemos los existentes
        if(dto.getReenactorsCount() != null) existingEvent.setReenactorsCount(dto.getReenactorsCount());
        if(dto.getVisitorsCount() != null) existingEvent.setVisitorsCount(dto.getVisitorsCount());

        // 🔥 CORRECCIÓN: Mapeo de la Asociación Organizadora extrayendo el ID del sub-objeto
        Long incomingAssociationId = dto.getOrganizingAssociation().getId();
        if (!existingEvent.getOrganizingAssociation().getId().equals(incomingAssociationId)) {
            Association newAssociation = associationService.findById(incomingAssociationId);
            existingEvent.setOrganizingAssociation(newAssociation);
        }

        // 4. Mapeo específico de las clases hijas
        if (existingEvent instanceof PublicEvent) {
            PublicEvent publicEvent = (PublicEvent) existingEvent;
            publicEvent.setPublicEntity(dto.getPublicEntity());
            publicEvent.setSubsidy(dto.getSubsidy());
        } else if (existingEvent instanceof PrivateEvent) {
            PrivateEvent privateEvent = (PrivateEvent) existingEvent;
            privateEvent.setSponsor(dto.getSponsor());
            privateEvent.setInitialBudget(dto.getInitialBudget());
            privateEvent.setOpenToPublic(dto.getOpenToPublic() != null ? dto.getOpenToPublic() : privateEvent.getOpenToPublic());
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

    private EventOutDto mapToOutDto(Event event) {
        EventOutDto dto = new EventOutDto();
        dto.setId(event.getId());
        dto.setEventCode(event.getEventCode());

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

        List<Long> attendingIds = event.getAttendances() == null ? List.of() :
                event.getAttendances().stream()
                        .map(attendance -> attendance.getAssociation().getId())
                        .collect(Collectors.toList());
        dto.setAttendingAssociationIds(attendingIds);

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