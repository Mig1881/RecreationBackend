package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Event;
import com.svalero.apirecreation.domain.dto.EventOutDto;
import com.svalero.apirecreation.service.CsvExportService;
import com.svalero.apirecreation.service.EventService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private CsvExportService csvExportService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventOutDto>> getAllEvents() {
        return new ResponseEntity<>(eventService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/published")
    public ResponseEntity<List<EventOutDto>> getPublishedEvents() {
        // Este endpoint es muy útil para que los Members vean solo los eventos confirmados
        return new ResponseEntity<>(eventService.findPublishedEvents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventOutDto> getEventById(@PathVariable Long id) {
        //Llamamos al nuevo método específico que nos devuelve el DTO y no la entidad
        return new ResponseEntity<>(eventService.findDtoById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventOutDto> createEvent(@RequestBody Event event) {
        // Jackson lee el JSON, crea la clase correcta (Public/Private), la guardamos
        // y el servicio nos devuelve la versión plana y segura (EventOutDto)
        EventOutDto createdEvent = eventService.save(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventOutDto> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        EventOutDto updatedEvent = eventService.update(id, event);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // MAGIA DE SEGURIDAD: Solo usuarios con estos roles pueden ejecutar el método
    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT')")
    @GetMapping("/{eventId}/export-participants")
    public void exportParticipantsCSV(@PathVariable Long eventId, HttpServletResponse response) throws IOException {

        // 1. Configurar la respuesta HTTP para que sea un archivo descargable
        response.setContentType("text/csv; charset=utf-8");

        // 2. Generar un nombre de archivo dinámico
        String filename = "tiradores_evento_" + eventId + "_" + LocalDate.now() + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // BOM para asegurar que Excel reconozca las tildes y caracteres UTF-8 correctamente
        response.getWriter().write('\ufeff');

        // 3. Ejecutar el motor de exportación
        csvExportService.exportEventParticipantsToCsv(eventId, response.getWriter());
    }
}

