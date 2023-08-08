package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.JoinForm;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.LoginForm;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result.JoinResult;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result.LoginResult;
import kr.co.seulchuksaeng.seulchuksaengweb.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @Value("${member.verifyCode}")
    private String verifyCode;

    private final MemberService memberService;

    @PostMapping("/join") //회원가입 ENDPOINT
    public JoinResult join(JoinForm joinForm) {
        try {
            memberService.verifiedMemberJoin(joinForm, verifyCode);
            Long joinId = memberService.join(joinForm);
        } catch (IllegalStateException e) {
            return new JoinResult("fail", e.getMessage());
        }

        return new JoinResult("success", "회원가입 성공");
    }

    @PostMapping("/login") //로그인 ENDPOINT
    public LoginResult login(LoginForm loginForm) {

        String jwtToken = "";

        try {
            jwtToken = memberService.login(loginForm);
        } catch (IllegalStateException e) {
            return new LoginResult("fail", e.getMessage(), jwtToken);
        }
        return new LoginResult("success", "로그인 성공", jwtToken);
    }


}
