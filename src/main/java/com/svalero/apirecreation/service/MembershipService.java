package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.domain.Membership;
import com.svalero.apirecreation.domain.dto.MembershipDTO;
import com.svalero.apirecreation.domain.dto.MembershipOutDto;
import com.svalero.apirecreation.exception.DuplicateEnrollmentException;
import com.svalero.apirecreation.exception.MembershipNotFoundException;
import com.svalero.apirecreation.repository.MembershipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private AssociationService associationService;

    @Autowired
    private MemberService memberService;

    public List<MembershipOutDto> findAll() {
        return membershipRepository.findAll().stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    public List<MembershipOutDto> getMembershipsByMemberId(Long memberId) {
        return membershipRepository.findByMemberId(memberId).stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    public List<MembershipOutDto> getMembershipsByAssociationId(Long associationId) {
        return membershipRepository.findByAssociationId(associationId).stream()
                .map(this::mapToOutDto)
                .collect(Collectors.toList());
    }

    public MembershipOutDto registerMembership(MembershipDTO dto) {
        if (membershipRepository.existsByAssociationIdAndMemberId(dto.getAssociationId(), dto.getMemberId())) {
            log.warn("Intento de vinculación duplicada: Asociación {} - Miembro {}", dto.getAssociationId(), dto.getMemberId());
            throw new DuplicateEnrollmentException("El miembro ya pertenece a esta asociación o tiene un expediente previo.");
        }

        Association association = associationService.findById(dto.getAssociationId());
        Member member = memberService.findById(dto.getMemberId());

        Membership membership = new Membership();
        membership.setAssociation(association);
        membership.setMember(member);

        membership.setStartDate(LocalDate.now());

        Membership savedMembership = membershipRepository.save(membership);
        log.info("Nuevo miembro vinculado: {} a la asociación {} con fecha de alta {}",
                member.getLastName(), association.getName(), membership.getStartDate());

        return mapToOutDto(savedMembership);
    }

    //SOFT DELETE (Baja lógica / Licenciar) 🔥
    public void discharge(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException("Vinculación no encontrada con ID: " + id));

        // Marcamos la fecha de baja
        membership.setEndDate(LocalDate.now());

        membershipRepository.save(membership);
        log.info("Baja lógica aplicada (Discharged) a la vinculación con ID: {} en fecha {}", id, membership.getEndDate());
    }

    // REACTIVATE (Alta de un veterano en reserva) 🔥
    public void reactivate(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException("Vinculación no encontrada con ID: " + id));

        // Borramos la fecha de fin para que vuelva al servicio activo
        membership.setEndDate(null);

        membershipRepository.save(membership);
        log.info("Recreador llamado a filas de nuevo. Adscripción ID: {} reactivada.", id);
    }

    // --- MÉTODO PRIVADO DE MAPEO ---
    private MembershipOutDto mapToOutDto(Membership membership) {
        return new MembershipOutDto(
                membership.getId(),
                membership.getAssociation().getId(),
                membership.getAssociation().getName(),
                membership.getMember().getId(),
                membership.getMember().getFirstName() + " " + membership.getMember().getLastName(),

                membership.getMember().getNationalId(),
                membership.getMember().getHistoricalRank(),
                membership.getMember().getWeaponLicense(),
                membership.getMember().getEmail(),

                membership.getStartDate(),
                membership.getEndDate()
        );
    }
}