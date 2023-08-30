package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 도메인 테스트")
class MemberTest {

    @Test
    @DisplayName("회원 객체 생성 테스트")
    public void createMember() throws Exception {
        //given
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        //when & then
        assertEquals(member.getId(), "testId");
        assertEquals(member.getPassword(), "testPw");
        assertEquals(member.getName(), "testName");
        assertEquals(member.getPhone(), "010-0000-0000");
        assertEquals(member.getGender(), Gender.MALE);
        assertEquals(member.getRole(), UserRole.USER);
        assertEquals(member.getSalt(), "testSalt");
        assertEquals(member.getWarnPoint(), 0);
    }

    @Test
    @DisplayName("회원 경고 부여 테스트")
    public void giveWarnPoint() throws Exception {
        //given
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        //when
        member.giveWarnPoint();
        //then
        assertEquals(member.getWarnPoint(), 1);
    }



}