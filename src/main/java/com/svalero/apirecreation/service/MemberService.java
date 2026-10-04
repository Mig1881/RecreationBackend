package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Member;
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
    private PasswordEncoder passwordEncoder; // Inyecto el BCrypt

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

    public Member save(Member member) {
        // Encriptamos la contraseña en texto plano que llega desde Postman antes de guardarla
        String hashedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(hashedPassword);

        return memberRepository.save(member);
    }

    public Member update(Long id, Member memberDetails) {
        Member existingMember = findById(id);

        existingMember.setNationalId(memberDetails.getNationalId());
        existingMember.setLastName(memberDetails.getLastName());
        existingMember.setFirstName(memberDetails.getFirstName());
        existingMember.setPhone(memberDetails.getPhone());
        existingMember.setAssociationPosition(memberDetails.getAssociationPosition());
        existingMember.setHistoricalRank(memberDetails.getHistoricalRank());
        existingMember.setWeaponLicense(memberDetails.getWeaponLicense());
        existingMember.setBirthDate(memberDetails.getBirthDate());
        existingMember.setEnrollmentDate(memberDetails.getEnrollmentDate());
        existingMember.setEmail(memberDetails.getEmail());
        existingMember.setRole(memberDetails.getRole());

        // Añadimos la actualización de la URL de Cloudinary
        existingMember.setImageUrl(memberDetails.getImageUrl());

        // La actualización de contraseña la mantenemos fuera por seguridad.
        // Solo actualizamos los datos básicos y la URL del perfil.

        return memberRepository.save(existingMember);
    }

    public void delete(Long id) {
        Member existingMember = findById(id);
        memberRepository.delete(existingMember);
    }
}