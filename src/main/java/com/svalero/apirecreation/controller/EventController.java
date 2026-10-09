package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.dto.EventDTO;
import com.svalero.apirecreation.domain.dto.EventOutDto;
import com.svalero.apirecreation.service.CsvExportService;
import com.svalero.apirecreation.service.EventService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return new ResponseEntity<>(eventService.findPublishedEvents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventOutDto> getEventById(@PathVariable Long id) {
        return new ResponseEntity<>(eventService.findDtoById(id), HttpStatus.OK);
    }

    //Protegido con @Valid y EventDTO
    @PostMapping
    public ResponseEntity<EventOutDto> createEvent(@Valid @RequestBody EventDTO dto) {
        EventOutDto createdEvent = eventService.save(dto);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    //Protegido con @Valid y EventDTO
    @PutMapping("/{id}")
    public ResponseEntity<EventOutDto> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO dto) {
        EventOutDto updatedEvent = eventService.update(id, dto);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRESIDENT')")
    @GetMapping("/{eventId}/export-participants")
    public void exportParticipantsCSV(@PathVariable Long eventId, HttpServletResponse response) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        response.setContentType("text/csv; charset=utf-8");

        String filename = "tiradores_evento_" + eventId + "_" + LocalDate.now() + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        response.getWriter().write('\ufeff');

        csvExportService.exportEventParticipantsToCsv(eventId, response.getWriter(), userEmail, isAdmin);
    }
}