package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.domain.dto.MemberDTO;
import com.svalero.apirecreation.domain.dto.MembershipOutDto;
import com.svalero.apirecreation.service.MemberService;
import com.svalero.apirecreation.service.MembershipService;
import jakarta.validation.Valid; // 🔥 IMPORTANTE
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // 🔥 Usamos @Valid y MemberDTO
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDTO dto) {
        Member createdMember = memberService.save(dto);
        return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
    }

    // 🔥 Usamos @Valid y MemberDTO
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDTO dto) {
        Member updatedMember = memberService.update(id, dto);
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me/memberships")
    public ResponseEntity<List<MembershipOutDto>> getMyMemberships(org.springframework.security.core.Authentication authentication) {
        String email = authentication.getName();
        Member currentMember = memberService.findByEmail(email);
        List<MembershipOutDto> myMemberships = membershipService.getMembershipsByMemberId(currentMember.getId());
        log.info("Enviando {} asociaciones al frontend para el usuario {}", myMemberships.size(), email);
        return new ResponseEntity<>(myMemberships, HttpStatus.OK);
    }
}