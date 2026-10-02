package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.service.AssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/associations")
public class AssociationController {

    @Autowired
    private AssociationService associationService;

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
