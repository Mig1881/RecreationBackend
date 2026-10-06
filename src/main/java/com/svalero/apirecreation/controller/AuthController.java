package com.svalero.apirecreation.controller;

import com.svalero.apirecreation.domain.Member;
import com.svalero.apirecreation.domain.dto.JwtResponse;
import com.svalero.apirecreation.domain.dto.LoginDto;
import com.svalero.apirecreation.domain.dto.SignupDto;
import com.svalero.apirecreation.repository.MemberRepository;
import com.svalero.apirecreation.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth") // Ajustado al prefijo /api/
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupDto signUpDto) {
        if (memberRepository.existsByEmail(signUpDto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: ¡El email ya está en uso!");
        }

        Member member = new Member();
        member.setNationalId(signUpDto.getNationalId());
        member.setFirstName(signUpDto.getFirstName());
        member.setLastName(signUpDto.getLastName());
        member.setEmail(signUpDto.getEmail());
        member.setPassword(encoder.encode(signUpDto.getPassword()));
        member.setPhone(signUpDto.getPhone());
        member.setBirthDate(signUpDto.getBirthDate());
//        member.setEnrollmentDate(LocalDate.now());
        member.setRole("MEMBER"); // Rol por defecto

        memberRepository.save(member);

        return ResponseEntity.ok("¡Recreador registrado con éxito!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        return ResponseEntity.ok(new JwtResponse(jwt, member.getId(), userDetails.getUsername(), roles));
    }
}
