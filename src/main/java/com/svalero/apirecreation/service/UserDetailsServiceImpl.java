package com.svalero.apirecreation.service;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Spring Security exige que los roles empiecen por "ROLE_" (ej. ROLE_ADMIN, ROLE_MEMBER)
        String roleName = member.getRole();
        if (roleName != null && !roleName.toUpperCase().startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName.toUpperCase();
        } else if (roleName == null) {
            roleName = "ROLE_MEMBER"; // Rol por defecto
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        return new org.springframework.security.core.userdetails.User(
                member.getEmail(),
                member.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
