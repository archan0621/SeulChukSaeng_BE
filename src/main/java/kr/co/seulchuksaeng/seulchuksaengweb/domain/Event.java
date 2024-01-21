package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "event_id")
    private Long eventId;

    private String title;

    private String location;

    private String description;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_member")
    private Member createMember;

    public Event(String title, String location, String description, Double latitude, Double longitude, Gender gender, LocalDateTime startTime, LocalDateTime endTime, String money, Member createMember) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gender = gender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.money = money;
        this.createMember = createMember;
    }

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<MemberEvent> matches = new ArrayList<MemberEvent>();

    public Event() {}

    public void update(String title, String location, String description, Gender gender, LocalDateTime startTime, LocalDateTime endTime, String money) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.gender = gender;
        this.startTime = startTime;
        this.endTime = endTime;
        this.money = money;
    }

    public void updateLocation(Double y, Double x) {
        this.latitude = y;
        this.longitude = x;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "eventId = " + eventId + ", " +
                "title = " + title + ", " +
                "location = " + location + ", " +
                "description = " + description + ", " +
                "latitude = " + latitude + ", " +
                "longitude = " + longitude + ", " +
                "gender = " + gender + ", " +
                "startTime = " + startTime + ", " +
                "endTime = " + endTime + ", " +
                "money = " + money + ")";
    }
}
