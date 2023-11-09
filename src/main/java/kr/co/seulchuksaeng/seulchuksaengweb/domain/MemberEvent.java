package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.EventAlreadyEndedException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.AlreadyAttendException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.AlreadyPurchasedException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.EventNotStartException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.WatingPurchaseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity @Getter
@Slf4j
public class MemberEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Attendance attend() {

        if (this.attend == Attendance.ATTEND || this.attend == Attendance.LATE) { // 이미 출결한 상태인 경우 예외 발생
            throw new AlreadyAttendException();
        }

        // 현재 시간과 경기 시작 시간 비교 후 참여 상태를 결정
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime eventStartTime = event.getStartTime();
        LocalDateTime eventEndTime = event.getEndTime(); // 경기 종료 시간 추가

        // 두 시간 간의 차이 계산
        Duration timeDifferenceStart = Duration.between(eventStartTime, currentTime);
        Duration timeDifferenceEnd = Duration.between(eventEndTime, currentTime);

        // 경기 시작 1시간 전에 요청하면 예외처리
        if (timeDifferenceStart.toMinutes() <= -60) {
            throw new EventNotStartException();
        }

        // 현재 시간이 경기 종료 시간을 지났으면 예외처리
        if (timeDifferenceEnd.toMinutes() >= 0) {
            throw new EventAlreadyEndedException();
        }

        // 늦으면 지각 처리
        if (timeDifferenceStart.toMinutes() >= 0) {
            this.attend = Attendance.LATE;
            return Attendance.LATE;
        }

        this.attend = Attendance.ATTEND;
        return Attendance.ATTEND;
    }

    public void purchaseRequest() {
        if (this.purchased.equals(PurchaseStatus.PURCHASED)) { // 납부 상태가 납부 완료인 경우 예외 발생
            throw new AlreadyPurchasedException();
        }

        if (this.purchased.equals(PurchaseStatus.WAITING)) { // 납부 상태가 확인 대기중인 경우 예외 발생
            throw new WatingPurchaseException();
        }

        this.purchased = PurchaseStatus.WAITING;
    }

    public void purchaseCheck() {
        if (this.purchased.equals(PurchaseStatus.PURCHASED)) { // 납부 상태가 납부 완료인 경우 예외 발생
            throw new AlreadyPurchasedException();
        }

        if (this.purchased.equals(PurchaseStatus.NOT_PURCHASED)) { // 납부 상태가 납부 안함인 경우 예외 발생
            throw new WatingPurchaseException();
        }

        this.purchased = PurchaseStatus.PURCHASED;
    }

    public MemberEvent() {}



}
