package com.svalero.apirecreation.repository;

import com.svalero.apirecreation.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByAssociationId(Long associationId);
    List<Membership> findByMemberId(Long memberId);
    boolean existsByAssociationIdAndMemberId(Long associationId, Long memberId);
}
