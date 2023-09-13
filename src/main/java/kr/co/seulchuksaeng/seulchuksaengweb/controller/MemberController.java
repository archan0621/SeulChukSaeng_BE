package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import com.github.archan0621.DiscordLogger;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.LogExecutionTime;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.UserAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.MemberForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.MemberResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.ExistMemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UnverifiedJoinException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.WrongPasswordException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Value("${member.verifyCode}")
    private String verifyCode;

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    DiscordLogger discordLogger = DiscordLogger.instance();

    @LogExecutionTime
    @PostMapping("/join") //회원가입 ENDPOINT
    public MemberResult.Join join(@RequestBody MemberForm.Join joinForm) {
        log.info("회원가입 요청이 발생하였습니다 - 닉네임 : {}", joinForm.getName());
        try {
            memberService.verifiedMemberJoin(joinForm, verifyCode);
            Long joinId = memberService.join(joinForm);
            log.info("회원가입 성공하였습니다 - 닉네임 : {}", joinForm.getName());
            return new MemberResult.Join("success", "회원가입 성공");
        } catch (ExistMemberException | UnverifiedJoinException e) { // 이미 멤버가 존재하거나, 인증코드가 맞지 않은 예외가 발생할 수 있음
            log.info("회원가입 실패하였습니다 - 닉네임 : {}, 실패 사유 : {}", joinForm.getName(), e.getMessage());
            return new MemberResult.Join("fail", e.getMessage());
        }
    }

    @LogExecutionTime
    @PostMapping("/login") //로그인 ENDPOINT
    public MemberResult.Login login(@RequestBody MemberForm.Login loginForm) {
        log.info("로그인 요청이 발생하였습니다 - 아이디 : {}", loginForm.getLoginId());
        try {
            String jwtToken = memberService.login(loginForm);
            log.info("로그인 성공하였습니다 - 아이디 : {}", loginForm.getLoginId());
            return new MemberResult.Login("success", "로그인 성공", jwtToken);
        } catch (UserNotFoundException | WrongPasswordException e) { // 멤머가 존재하지 않거나 비밀번호가 일치하지 않는 예외가 발생할 수 있음
            log.info("로그인 실패하였습니다 - 아이디 : {}, 실패 사유 : {}", loginForm.getLoginId(), e.getMessage());
            return new MemberResult.Login("fail", e.getMessage(), null);
        }
    }

    @UserAuthorize
    @LogExecutionTime
    @GetMapping("/getUserName") // 유저 이름을 불러오면서 초기 Jwt 토큰 정상 발급 확인
    public MemberResult.GetUserName getUserName(@AuthenticationPrincipal User user) {
        log.info("Jwt 로그인 토큰 정상발급 확인 요청이 발생하였습니다 - 아이디 : {}", user.getUsername());
        try {
            Member member = memberRepository.findMemberById(user.getUsername());
            log.info("Jwt 로그인 토큰 정상발급 확인 완료 - 아이디 : {}", user.getUsername());
            return new MemberResult.GetUserName("success", member.getName(), member.getRole());
        } catch (Exception e) {
            //Jwt 토큰이 잘못되었거나 유저를 불러오지 못한 경우에 발생
            log.info("Jwt 로그인 토큰 정상발급 확인 실패 - 아이디 : {}", user.getUsername());
            return new MemberResult.GetUserName("failed", "에러발생! 유저 조회에 실패하였습니다.", null);
        }
    }

}
