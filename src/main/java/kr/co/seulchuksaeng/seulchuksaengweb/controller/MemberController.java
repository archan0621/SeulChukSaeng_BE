package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.JoinForm;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result.JoinResult;
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

    @PostMapping("/join")
    public JoinResult join(JoinForm joinForm) {
        log.info("회원가입 인증코드 = {}", verifyCode);
        try {
            memberService.verifiedMemberJoin(joinForm, verifyCode);
            Long joinId = memberService.join(joinForm);
        } catch (IllegalStateException e) {
            return new JoinResult("fail", e.getMessage());
        }

        return new JoinResult("success", "회원가입 성공");
    }

}
