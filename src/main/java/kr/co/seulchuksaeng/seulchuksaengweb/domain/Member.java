package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberEvent> matches = new ArrayList<MemberEvent>();



}
