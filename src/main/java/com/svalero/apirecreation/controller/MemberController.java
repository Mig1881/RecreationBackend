package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // GET: /api/members
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // GET: /api/members/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // POST: /api/members
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member createdMember = memberService.save(member);
        return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
    }

    // PUT: /api/members/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        Member updatedMember = memberService.update(id, member);
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    // DELETE: /api/members/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
