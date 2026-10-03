package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.exception.EventNotFoundException;
import com.svalero.apirecreation.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AssociationService associationService;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con ID: " + id));
    }

    public List<Event> findPublishedEvents() {
        return eventRepository.findByPublishedTrue();
    }

    public Event save(Event event) {
        //Se extrae el ID de la asociación que viene en el JSON
        Long associationId = event.getOrganizingAssociation().getId();

        //Se busca la asociación real en la base de datos (lanzará 404 si no existe)
        Association organizingAssociation = associationService.findById(associationId);

        //Se enlaza la entidad completa y guardamos
        event.setOrganizingAssociation(organizingAssociation);
        return eventRepository.save(event);
    }

    public Event update(Long id, Event eventDetails) {
        Event existingEvent = findById(id);

        //campos base (comunes a todos los eventos)
        existingEvent.setEventCode(eventDetails.getEventCode());
        existingEvent.setCountry(eventDetails.getCountry());
        existingEvent.setCity(eventDetails.getCity());
        existingEvent.setCost(eventDetails.getCost());
        existingEvent.setStartDate(eventDetails.getStartDate());
        existingEvent.setEndDate(eventDetails.getEndDate());
        existingEvent.setPublished(eventDetails.getPublished());
        existingEvent.setReenactorsCount(eventDetails.getReenactorsCount());
        existingEvent.setVisitorsCount(eventDetails.getVisitorsCount());

        //Se actualiza la asociación si ha cambiado
        Long newAssociationId = eventDetails.getOrganizingAssociation().getId();
        if (!existingEvent.getOrganizingAssociation().getId().equals(newAssociationId)) {
            Association newAssociation = associationService.findById(newAssociationId);
            existingEvent.setOrganizingAssociation(newAssociation);
        }

        // Spring Data JPA actualizará
        // automáticamente la tabla public_events o private_events según corresponda.
        return eventRepository.save(existingEvent);
    }

    public void delete(Long id) {
        Event existingEvent = findById(id);
        eventRepository.delete(existingEvent);
    }
}
