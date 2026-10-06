package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.dto.MembershipOutDto;
import com.svalero.apirecreation.service.AssociationService;
import com.svalero.apirecreation.service.MembershipService;
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

    // GET: /api/associations
    @GetMapping
    public ResponseEntity<List<Association>> getAllAssociations() {
        List<Association> associations = associationService.findAll();
        return new ResponseEntity<>(associations, HttpStatus.OK);
    }

    // GET: /api/associations/{id}
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

    // POST: /api/associations
    @PostMapping
    public ResponseEntity<Association> createAssociation(@RequestBody Association association) {
        Association createdAssociation = associationService.save(association);
        return new ResponseEntity<>(createdAssociation, HttpStatus.CREATED);
    }

    // PUT: /api/associations/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Association> updateAssociation(@PathVariable Long id, @RequestBody Association association) {
        Association updatedAssociation = associationService.update(id, association);
        return new ResponseEntity<>(updatedAssociation, HttpStatus.OK);
    }

    // DELETE: /api/associations/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssociation(@PathVariable Long id) {
        associationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
