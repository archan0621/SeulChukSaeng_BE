package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"}, name = "ID_UNIQUE")})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", nullable = false, length = 13)
    private String phone;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Column(name = "warn_point")
    private int warnPoint;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberEvent> matches = new ArrayList<MemberEvent>();

    public Member(String id, String password, String name, String phone, Gender gender, UserRole role, String salt, int warnPoint) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.role = role;
        this.salt = salt;
        this.warnPoint = warnPoint;
    }

    public Member(Long memberId) {
        this.memberId = memberId;
    }

    public void giveWarnPoint() {
        this.warnPoint++;
    }

    public void giveWarnPoint(Integer warnPoint) {
        this.warnPoint += warnPoint;
    }

    public Member () {}
}
