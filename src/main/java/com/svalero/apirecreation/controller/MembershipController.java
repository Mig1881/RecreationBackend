package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Membership;
import com.svalero.apirecreation.domain.dto.MembershipDTO;
import com.svalero.apirecreation.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public ResponseEntity<List<Membership>> getAllMemberships() {
        return new ResponseEntity<>(membershipService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Membership> createMembership(@RequestBody MembershipDTO dto) {
        Membership created = membershipService.registerMembership(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@PathVariable Long id) {
        membershipService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
