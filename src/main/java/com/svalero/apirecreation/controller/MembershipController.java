package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.dto.MembershipDTO;
import com.svalero.apirecreation.domain.dto.MembershipOutDto;
import com.svalero.apirecreation.service.MembershipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public ResponseEntity<List<MembershipOutDto>> getAllMemberships() {
        return new ResponseEntity<>(membershipService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MembershipOutDto> createMembership(@RequestBody MembershipDTO dto) {
        MembershipOutDto created = membershipService.registerMembership(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@PathVariable Long id) {
        membershipService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}