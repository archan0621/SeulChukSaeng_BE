package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.EventAlreadyEndedException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.AlreadyAttendException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.AlreadyPurchasedException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.EventNotStartException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.WatingPurchaseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MemberEventTest {

    @Test
    @DisplayName("멤버 이벤트 출석 테스트 / 정상 출석")
    void attendTest() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ABSENT, PurchaseStatus.NOT_PURCHASED);

        memberEvent.attend(LocalDateTime.of(2023, 1, 1, 11, 50, 0));
        assertEquals(memberEvent.getAttend(), Attendance.ATTEND);
    }

    @Test
    @DisplayName("멤버 이벤트 출석 테스트 / 지각")
    void attendLateTest() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ABSENT, PurchaseStatus.NOT_PURCHASED);

        memberEvent.attend(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
        assertEquals(memberEvent.getAttend(), Attendance.LATE);
    }

    @Test
    @DisplayName("멤버 이벤트 출석 테스트 / 경기시간 1시간 전 출석")
    void attendBeforeEventTest() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ABSENT, PurchaseStatus.NOT_PURCHASED);

        assertThrows(EventNotStartException.class, () -> {
            memberEvent.attend(LocalDateTime.of(2023, 1, 1, 11, 0, 0));
        });
    }

    @Test
    @DisplayName("멤버 이벤트 출석 테스트 / 경기시간 종료 후 출석")
    void attendAfterEventTest() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ABSENT, PurchaseStatus.NOT_PURCHASED);

        assertThrows(EventAlreadyEndedException.class, () -> {
            memberEvent.attend(LocalDateTime.of(2023, 1, 1, 14, 0, 0));
        });
    }

    @Test
    @DisplayName("직권출석 처리 테스트")
    void forceAttend() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ABSENT, PurchaseStatus.NOT_PURCHASED);

        memberEvent.forceAttend();
        Attendance attend = memberEvent.getAttend();
        assertEquals(attend, Attendance.ATTEND);
    }

    @Test
    @DisplayName("직권출석 처리 테스트 / 이미 출석함 예외")
    void forceAlreadyAttend() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ATTEND, PurchaseStatus.NOT_PURCHASED);
        assertThrows(AlreadyAttendException.class, memberEvent::forceAttend);
    }

    @Test
    @DisplayName("회비 납부 요청 테스트 / 정상")
    void purchaseRequest() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ATTEND, PurchaseStatus.NOT_PURCHASED);

        memberEvent.purchaseRequest();

        PurchaseStatus purchased = memberEvent.getPurchased();
        assertEquals(purchased, PurchaseStatus.WAITING);
    }

    @Test
    @DisplayName("회비 납부 요청 테스트 / 이미 납부가 된 경우")
    void alreadyPurchasedRequest() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ATTEND, PurchaseStatus.PURCHASED);

        assertThrows(AlreadyPurchasedException.class, memberEvent::purchaseRequest);
    }

    @Test
    @DisplayName("회비 납부 요청 테스트 / 납부 확인 대기중인 경우")
    void waitingPurchaseRequest() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ATTEND, PurchaseStatus.WAITING);

        assertThrows(WatingPurchaseException.class, memberEvent::purchaseRequest);
    }

    @Test
    @DisplayName("회비 납부 요청 확인 테스트")
    void purchaseCheck() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ATTEND, PurchaseStatus.WAITING);

        memberEvent.purchaseCheck();
        PurchaseStatus purchased = memberEvent.getPurchased();
        assertEquals(purchased, PurchaseStatus.PURCHASED);
    }

    @Test
    @DisplayName("회비 납부 요청 확인 테스트 / 이미 회비를 납부한 경우")
    void alreadyPurchasedCheck() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ATTEND, PurchaseStatus.PURCHASED);

        assertThrows(AlreadyPurchasedException.class, memberEvent::purchaseCheck);
    }

    @Test
    @DisplayName("회비 납부 요청 확인 테스트 / 회비 납부 요청이 없는 경우")
    void notPurchasedCheck() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 1D, 2D, Gender.MALE,
                LocalDateTime.of(2023, 1, 1, 12, 0, 0),
                LocalDateTime.of(2023, 1, 1, 14, 0, 0),
                "7200", member);
        MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ATTEND, PurchaseStatus.NOT_PURCHASED);

        assertThrows(WatingPurchaseException.class, memberEvent::purchaseCheck);
    }

}