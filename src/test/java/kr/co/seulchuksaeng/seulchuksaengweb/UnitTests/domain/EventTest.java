package kr.co.seulchuksaeng.seulchuksaengweb.UnitTests.domain;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("이벤트 도메인 테스트")
class EventTest {

    @Test
    @DisplayName("이벤트 객체 생성 테스트")
    public void createEvent() throws Exception {
        //given
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", null, null, Gender.MALE, null, null, "testMoney", member);
        //when & then
        assertEquals(event.getTitle(), "testTitle");
        assertEquals(event.getLocation(), "testLocation");
        assertEquals(event.getDescription(), "testDesc");
        assertEquals(event.getGender(), Gender.MALE);
        assertEquals(event.getMoney(), "testMoney");
        assertEquals(event.getCreateMember(), member);
    }
    @Test
    @DisplayName("이벤트 객체 수정 테스트")
    void update() {
        //given
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", null, null, Gender.MALE, null, null, "testMoney", member);
        //when
        event.update("updateTitle", "updateLocation", "updateDesc", Gender.FEMALE, null, null, "updateMoney");
        //then
        assertEquals(event.getTitle(), "updateTitle");
        assertEquals(event.getLocation(), "updateLocation");
        assertEquals(event.getDescription(), "updateDesc");
        assertEquals(event.getGender(), Gender.FEMALE);
        assertEquals(event.getMoney(), "updateMoney");
    }

    @Test
    @DisplayName("위도 경도 수정 테스트")
    void updateLocation() {
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        Event event = new Event("testTitle", "testLocation", "testDesc", 31D, 30D, Gender.MALE, null, null, "testMoney", member);

        event.updateLocation(123D, 456D);

        assertEquals(event.getLatitude(), 123D);
        assertEquals(event.getLongitude(), 456D);

    }
}