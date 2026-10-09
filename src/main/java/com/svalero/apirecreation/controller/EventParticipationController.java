package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.dto.EventParticipationDTO;
import com.svalero.apirecreation.domain.dto.EventParticipationOutDto;
import com.svalero.apirecreation.service.EventParticipationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/participations")
public class EventParticipationController {

    @Autowired
    private EventParticipationService participationService;

    @GetMapping
    public ResponseEntity<List<EventParticipationOutDto>> getAllParticipations() {
        return new ResponseEntity<>(participationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventParticipationOutDto>> getByEvent(@PathVariable Long eventId) {
        return new ResponseEntity<>(participationService.getParticipationsByEvent(eventId), HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<EventParticipationOutDto>> getByMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(participationService.getParticipationsByMember(memberId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventParticipationOutDto> createParticipation(@Valid @RequestBody EventParticipationDTO participationDTO) {
        EventParticipationOutDto createdParticipation = participationService.enrollMember(participationDTO);

        log.info("Inscripción devuelta al cliente correctamente para el evento ID: {}", participationDTO.getEventId());

        return new ResponseEntity<>(createdParticipation, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long id) {
        participationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}