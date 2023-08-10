package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
public class Event {

    @Id @GeneratedValue
    @JoinColumn(name = "event_id")
    private Long eventId;

    private String title;

    private String location;

    private String description;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member createMember;

    public Event(String title, String location, String description, Gender gender, LocalDateTime startTime, LocalDateTime endTime, String money, Member createMember) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.gender = gender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.money = money;
        this.createMember = createMember;
    }

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<MemberEvent> matches = new ArrayList<MemberEvent>();

}
