package kr.co.seulchuksaeng.seulchuksaengweb.domain;

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
        Event event = new Event("testTitle", "testLocation", "testDesc", Gender.MALE, null, null, "testMoney", member);
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
        Event event = new Event("testTitle", "testLocation", "testDesc", Gender.MALE, null, null, "testMoney", member);
        //when
        event.update("updateTitle", "updateLocation", "updateDesc", Gender.FEMALE, null, null, "updateMoney");
        //then
        assertEquals(event.getTitle(), "updateTitle");
        assertEquals(event.getLocation(), "updateLocation");
        assertEquals(event.getDescription(), "updateDesc");
        assertEquals(event.getGender(), Gender.FEMALE);
        assertEquals(event.getMoney(), "updateMoney");
    }
}