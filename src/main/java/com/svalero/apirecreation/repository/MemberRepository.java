package com.svalero.apirecreation.repository;

import com.svalero.apirecreation.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNationalId(String nationalId);

    // Validaciones de existencia rápida antes de guardar
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
}
