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

    // 🔥 1. ENDPOINT PARA BAJA LÓGICA (Licenciar / Soft Delete) 🔥
    @PutMapping("/{id}/discharge")
    public ResponseEntity<Void> dischargeMembership(@PathVariable Long id) {
        membershipService.discharge(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 🔥 2. ENDPOINT PARA REACTIVAR (Vuelta al servicio activo) 🔥
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateMembership(@PathVariable Long id) {
        membershipService.reactivate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}