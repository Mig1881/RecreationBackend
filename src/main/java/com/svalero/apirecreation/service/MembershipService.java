package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Association;
import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.domain.Membership;
import com.svalero.apirecreation.domain.dto.MembershipDTO;
import com.svalero.apirecreation.exception.DuplicateEnrollmentException;
import com.svalero.apirecreation.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private AssociationService associationService;
    @Autowired
    private MemberService memberService;

    public List<Membership> findAll() {
        return membershipRepository.findAll();
    }

    public Membership registerMembership(MembershipDTO dto) {
        if (membershipRepository.existsByAssociationIdAndMemberId(dto.getAssociationId(), dto.getMemberId())) {
            throw new DuplicateEnrollmentException("El miembro ya pertenece a esta asociación.");
        }

        Association association = associationService.findById(dto.getAssociationId());
        Member member = memberService.findById(dto.getMemberId());

        Membership membership = new Membership();
        membership.setAssociation(association);
        membership.setMember(member);

        return membershipRepository.save(membership);
    }

    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }
}
