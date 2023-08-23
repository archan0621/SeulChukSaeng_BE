package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.AlreadyAttendException;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity @Getter
public class MemberEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_event_id")
    private Long memberEventId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
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

    public void attend() {

        if (this.attend == Attendance.ATTEND || this.attend == Attendance.LATE) { //이미 출결한 상태인 경우 예외 발생
            throw new AlreadyAttendException();
        }

        //현재 시간과 경기 시작 시작 비교 후 참여 상태를 결정
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime eventStartTime = event.getStartTime();

        // 두 시간 간의 차이 계산
        Duration timeDifference = Duration.between(eventStartTime, currentTime);

        // 20분 이상이면 지각처리
        if (timeDifference.toMinutes() >= 20) {
            this.attend = Attendance.LATE;
        } else {
            this.attend = Attendance.ATTEND;
        }
    }

    public MemberEvent() {}
}
