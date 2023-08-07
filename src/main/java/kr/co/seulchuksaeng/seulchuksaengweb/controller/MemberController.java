package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.JoinForm;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result.JoinResult;
import kr.co.seulchuksaeng.seulchuksaengweb.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public JoinResult join(JoinForm joinForm) {

        try {
            Long joinId = memberService.join(joinForm);
        } catch (IllegalStateException e) {
            return new JoinResult("fail", e.getMessage());
        }

        return new JoinResult("success", "회원가입 성공");
    }

}
