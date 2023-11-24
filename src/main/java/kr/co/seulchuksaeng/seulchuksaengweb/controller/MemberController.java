package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import com.github.archan0621.DiscordLogger;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.AdminAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.LogExecutionTime;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.UserAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.MemberForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.MemberResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberDetailInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberListInnerResult;
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

import java.util.List;

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
        log.info("회원가입 요청이 발생하였습니다 - 닉네임 : {}", joinForm.name());
        try {
            memberService.verifiedMemberJoin(joinForm, verifyCode);
            Long joinId = memberService.join(joinForm);
            log.info("회원가입 성공하였습니다 - 닉네임 : {}", joinForm.name());
            return new MemberResult.Join("success", "회원가입 성공");
        } catch (ExistMemberException | UnverifiedJoinException e) { // 이미 멤버가 존재하거나, 인증코드가 맞지 않은 예외가 발생할 수 있음
            log.info("회원가입 실패하였습니다 - 닉네임 : {}, 실패 사유 : {}", joinForm.name(), e.getMessage());
            return new MemberResult.Join("fail", e.getMessage());
        }
    }

    @LogExecutionTime
    @PostMapping("/login") //로그인 ENDPOINT
    public MemberResult.Login login(@RequestBody MemberForm.Login loginForm) {
        log.info("로그인 요청이 발생하였습니다 - 아이디 : {}", loginForm.loginId());
        try {
            String jwtToken = memberService.login(loginForm);
            log.info("로그인 성공하였습니다 - 아이디 : {}", loginForm.loginId());
            return new MemberResult.Login("success", "로그인 성공", jwtToken);
        } catch (UserNotFoundException | WrongPasswordException e) { // 멤버가 존재하지 않거나 비밀번호가 일치하지 않는 예외가 발생할 수 있음
            log.info("로그인 실패하였습니다 - 아이디 : {}, 실패 사유 : {}", loginForm.loginId(), e.getMessage());
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

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/memberList")
    public MemberResult.MemberList memberList(@AuthenticationPrincipal User user) {
        log.info("멤버 목록 조회 요청이 발생하였습니다 - 아이디 : {}", user.getUsername());
        try {
            List<MemberListInnerResult> allMember = memberService.getMemberList();
            log.info("멤버 목록 조회에 성공하였습니다 - 아이디 : {}", user.getUsername());
            return new MemberResult.MemberList("success", "유저 목록 조회 성공", allMember);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new MemberResult.MemberList("fail", "사용자 목록 조회에 실패하였습니다", null);
        }
    }

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/memberDetail")
    public MemberResult.Detail memberDetail(@AuthenticationPrincipal User user, @RequestBody MemberForm.Detail detailForm) {
        log.info("멤버 상세 조회 요청이 발생하였습니다 - 아이디 : {}", user.getUsername());
        try {
            Member member = memberRepository.findMemberByPK(detailForm.memberId());
            MemberDetailInnerResult.MemberInfo memberDetail = memberService.getMemberDetail(member);
            MemberDetailInnerResult.rate memberRate = memberService.getMemberRate(member);
            List<MemberDetailInnerResult.joinedGame> memberJoinedGame = memberService.getMemberJoinedGame(member);
            log.info("멤버 상세 조회 요청에 성공하였습니다 - 아이디 : {}", user.getUsername());
            return new MemberResult.Detail("success", "멤버 상세 조회에 성공하였습니다", memberDetail, memberRate, memberJoinedGame);
        } catch (UserNotFoundException e) {
            log.info("멤버 상세 조회 요청에 실패하였습니다 - 아이디 : {}", user.getUsername());
            return new MemberResult.Detail("fail", e.getMessage(), null, null, null);
        } catch (Exception e) {
            log.info("멤버 상세 조회 요청에 실패하였습니다 - 아이디 : {}", user.getUsername());
            return new MemberResult.Detail("fail", "알 수 없는 오류로 실패했습니다, 종하탓임 이거", null, null, null);
        }
    }

}