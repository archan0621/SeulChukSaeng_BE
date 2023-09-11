package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.MemberForm;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.ExistMemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UnverifiedJoinException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.security.Crypto;
import kr.co.seulchuksaeng.seulchuksaengweb.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("회원 서비스 테스트")
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private Crypto crypto;
    @Autowired private TokenProvider tokenProvider;

    @Test
    @DisplayName("회원가입 테스트")
    public void join() throws Exception {
        //given
        MemberForm.Join joinForm = new MemberForm.Join("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, "1234");
        //when
        Long join = memberService.join(joinForm);
        //then
        assertEquals(join, memberRepository.findMemberById("testId").getMemberId());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void duplicateJoin() throws Exception {
        //given
        MemberForm.Join joinForm = new MemberForm.Join("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, "1234");
        MemberForm.Join joinForm2 = new MemberForm.Join("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, "1234");
        //when
        Long join = memberService.join(joinForm);
        //then
        assertThrows(ExistMemberException.class, () -> memberService.join(joinForm2));
    }

    @Test
    @DisplayName("잘못된 인증 코드 테스트")
    public void wrongVerifyJoin() throws Exception {
        //given
        MemberForm.Join joinForm = new MemberForm.Join("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, "asdfasdf");
        //when & then
        assertThrows(UnverifiedJoinException.class, () -> memberService.verifiedMemberJoin(joinForm, "1234"));
    }

    @Test
    @DisplayName("로그인 테스트")
    public void login() throws Exception {
        //given
        MemberForm.Join joinForm = new MemberForm.Join("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, "1234");
        Long join = memberService.join(joinForm);
        MemberForm.Login loginForm = new MemberForm.Login("testId", "testPw");
        //when
        String login = memberService.login(loginForm);
        //then
        assertNotNull(login);
    }



}