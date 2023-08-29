package com.devtalk.member.memberservice.member.domain.member;

import com.devtalk.member.memberservice.member.application.port.in.dto.SignUpReq;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20, unique = true)
    private String phoneNumber;

    public static Member createMember(SignUpReq req, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .memberType(req.getMemberType())
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phoneNumber(req.getPhoneNumber())
                .build();
        return member;
    }

}
