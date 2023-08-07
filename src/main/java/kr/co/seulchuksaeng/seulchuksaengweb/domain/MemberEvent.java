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

    private Boolean attend;

    private Boolean purchased;

}
