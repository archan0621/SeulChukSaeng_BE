package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.annotation.UserAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.JoinForm;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.LoginForm;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result.GetUserNameResult;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result.JoinResult;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result.LoginResult;
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

    @PostMapping("/join") //회원가입 ENDPOINT
    public JoinResult join(@RequestBody JoinForm joinForm) {
        try {
            memberService.verifiedMemberJoin(joinForm, verifyCode);
            Long joinId = memberService.join(joinForm);
        } catch (IllegalStateException e) {
            return new JoinResult("fail", e.getMessage());
        }

        return new JoinResult("success", "회원가입 성공");
    }

    @PostMapping("/login") //로그인 ENDPOINT
    public LoginResult login(@RequestBody LoginForm loginForm) {

        String jwtToken = "";

        try {
            jwtToken = memberService.login(loginForm);
        } catch (IllegalStateException e) {
            return new LoginResult("fail", e.getMessage(), jwtToken);
        }
        return new LoginResult("success", "로그인 성공", jwtToken);
    }

    @GetMapping("/getUserName") // 유저 이름 불러오기
    @UserAuthorize
    public GetUserNameResult getUserName(@AuthenticationPrincipal User user) {
        log.info("Jwt 로그인 토큰 정상발급 확인 완료 : {}", user.getUsername());
        return new GetUserNameResult("success", user.getUsername());
    }

}
