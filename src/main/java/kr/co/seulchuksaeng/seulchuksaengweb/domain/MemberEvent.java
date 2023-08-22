package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity @Getter
public class MemberEvent {

    @Id @GeneratedValue
    @Column(name = "member_event_id")
    private Long memberEventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    private Attendance attend;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchased;

    public MemberEvent(Member member, Event event, Attendance attend, PurchaseStatus purchased) {
        this.member = member;
        this.event = event;
        this.attend = attend;
        this.purchased = purchased;
    }

    public MemberEvent() {}
}
