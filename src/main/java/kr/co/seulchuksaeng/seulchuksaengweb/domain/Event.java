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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<MemberEvent> matches = new ArrayList<MemberEvent>();

}
