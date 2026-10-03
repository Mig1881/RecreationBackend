package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.EventParticipation;
import com.svalero.apirecreation.domain.dto.EventParticipationDTO;
import com.svalero.apirecreation.service.EventParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
public class EventParticipationController {

    @Autowired
    private EventParticipationService participationService;

    @GetMapping
    public ResponseEntity<List<EventParticipation>> getAllParticipations() {
        return new ResponseEntity<>(participationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventParticipation>> getByEvent(@PathVariable Long eventId) {
        return new ResponseEntity<>(participationService.getParticipationsByEvent(eventId), HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<EventParticipation>> getByMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(participationService.getParticipationsByMember(memberId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventParticipation> createParticipation(@RequestBody EventParticipationDTO participationDTO) {
        EventParticipation createdParticipation = participationService.enrollMember(participationDTO);
        return new ResponseEntity<>(createdParticipation, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long id) {
        participationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}