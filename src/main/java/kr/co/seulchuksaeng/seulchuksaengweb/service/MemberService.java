package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.JoinForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.LoginForm;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.ExistMemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UnverifiedJoinException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.WrongPasswordException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.security.Crypto;
import kr.co.seulchuksaeng.seulchuksaengweb.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final Crypto crypto;
    private final TokenProvider tokenProvider;

    @Transactional
    public Long join(JoinForm joinForm) {
        //중복 회원 검증
        validateDuplicateMember(joinForm);

        //비밀번호 암호화
        String salt = crypto.getSalt();
        String saltPassword = crypto.saltPassword(joinForm.getPassword(), salt);

        // 암호화한 정보로 회원 저장
        Member member = new Member(joinForm.getId(), saltPassword, joinForm.getName(), joinForm.getPhone(), joinForm.getGender(), UserRole.USER, salt, 0);
        memberRepository.save(member);

        return member.getMemberId();
    }

    public String login(LoginForm loginForm) {
        Member member = new Member();

        //회원 존재 여부 확인
        try {
            member = memberRepository.findMemberById(loginForm.getLoginId());
        } catch (IndexOutOfBoundsException e) {
            throw new UserNotFoundException();
        }

        //회원이 있다면 비밀번호 암호화
        String saltPassword = crypto.saltPassword(loginForm.getPassword(), member.getSalt());

        //비밀번호 일치 여부 확인
        if(!Objects.equals(saltPassword, member.getPassword())) {
            throw new WrongPasswordException();
        }

        // 회원 존재 및 비밀번호 일치 확인시 JWT 토큰 발급
        return tokenProvider.createToken(String.format("%s:%s", member.getId(), member.getRole()));
    }


    private void validateDuplicateMember(JoinForm joinForm) {
        //EXCEPTION
        try {
            Member member = memberRepository.findMemberById(joinForm.getId());
            throw new ExistMemberException();
        } catch (UserNotFoundException e) {
            return;
        }
    }

    public void verifiedMemberJoin(JoinForm joinForm, String verifyCode) {
        //회원 인증코드 검증
        String verifyCodeUser = joinForm.getVerifyCode();
        if (!Objects.equals(verifyCode, verifyCodeUser)) {
            log.info("현재 인증코드 : {}, 입력받은 인증코드 : {} - 회원가입 거부", verifyCode, verifyCodeUser);
            throw new UnverifiedJoinException();
        }
        log.info("현재 인증코드 : {}, 입력받은 인증코드 : {} - 회원가입 승인", verifyCode, verifyCodeUser);

    }
}
