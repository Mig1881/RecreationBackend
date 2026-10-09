package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.dto.AssociationDTO;
import com.svalero.apirecreation.domain.dto.MembershipOutDto;
import com.svalero.apirecreation.service.AssociationService;
import com.svalero.apirecreation.service.MembershipService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/associations")
public class AssociationController {

    @Autowired
    private AssociationService associationService;

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public ResponseEntity<List<Association>> getAllAssociations() {
        List<Association> associations = associationService.findAll();
        return new ResponseEntity<>(associations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Association> getAssociationById(@PathVariable Long id) {
        Association association = associationService.findById(id);
        return new ResponseEntity<>(association, HttpStatus.OK);
    }

    @GetMapping("/{id}/memberships")
    public ResponseEntity<List<MembershipOutDto>> getAssociationMemberships(@PathVariable Long id) {
        List<MembershipOutDto> troops = membershipService.getMembershipsByAssociationId(id);
        log.info("Enviando {} expedientes para la asociación con ID: {}", troops.size(), id);
        return new ResponseEntity<>(troops, HttpStatus.OK);
    }

    // 🔥 Usamos @Valid y AssociationDTO
    @PostMapping
    public ResponseEntity<Association> createAssociation(@Valid @RequestBody AssociationDTO dto) {
        Association createdAssociation = associationService.save(dto);
        return new ResponseEntity<>(createdAssociation, HttpStatus.CREATED);
    }

    // 🔥 Usamos @Valid y AssociationDTO
    @PutMapping("/{id}")
    public ResponseEntity<Association> updateAssociation(@PathVariable Long id, @Valid @RequestBody AssociationDTO dto) {
        Association updatedAssociation = associationService.update(id, dto);
        return new ResponseEntity<>(updatedAssociation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssociation(@PathVariable Long id) {
        associationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}