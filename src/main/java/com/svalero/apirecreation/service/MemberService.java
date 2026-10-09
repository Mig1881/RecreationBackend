package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.domain.dto.MemberDTO;
import com.svalero.apirecreation.exception.MemberNotFoundException;
import com.svalero.apirecreation.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Miembro no encontrado con ID: " + id));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("Miembro no encontrado con email: " + email));
    }

    //Recibe el DTO y crea la Entidad
    public Member save(MemberDTO dto) {
        Member member = new Member();
        member.setNationalId(dto.getNationalId());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setRole(dto.getRole());
        member.setPhone(dto.getPhone());
        member.setAssociationPosition(dto.getAssociationPosition());
        member.setHistoricalRank(dto.getHistoricalRank());
        member.setWeaponLicense(dto.getWeaponLicense());
        member.setBirthDate(dto.getBirthDate());
        member.setImageUrl(dto.getImageUrl());

        // Encriptamos la contraseña del DTO
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(hashedPassword);

        return memberRepository.save(member);
    }

    //Recibe el DTO y actualiza la Entidad
    public Member update(Long id, MemberDTO dto) {
        Member existingMember = findById(id);

        existingMember.setNationalId(dto.getNationalId());
        existingMember.setFirstName(dto.getFirstName());
        existingMember.setLastName(dto.getLastName());
        existingMember.setEmail(dto.getEmail());
        existingMember.setRole(dto.getRole());
        existingMember.setPhone(dto.getPhone());
        existingMember.setAssociationPosition(dto.getAssociationPosition());
        existingMember.setHistoricalRank(dto.getHistoricalRank());
        existingMember.setWeaponLicense(dto.getWeaponLicense());
        existingMember.setBirthDate(dto.getBirthDate());
        existingMember.setImageUrl(dto.getImageUrl());

        // La actualización de contraseña la mantenemos fuera por seguridad,

        return memberRepository.save(existingMember);
    }

    public void delete(Long id) {
        Member existingMember = findById(id);
        memberRepository.delete(existingMember);
    }
}