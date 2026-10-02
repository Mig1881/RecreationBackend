package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.exception.MemberNotFoundException;
import com.svalero.apirecreation.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // La actualización de contraseña e imagen (Cloudinary) la solemos manejar
        // en métodos separados más adelante por seguridad, así que aquí actualizamos
        // solo los datos básicos del perfil.

        return memberRepository.save(existingMember);
    }

    public void delete(Long id) {
        Member existingMember = findById(id);
        memberRepository.delete(existingMember);
    }
}
