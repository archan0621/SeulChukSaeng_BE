package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.controller.form.LoginForm;
import kr.co.seulchuksaeng.seulchuksaengweb.controller.form.MemberForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MemberController {

    @GetMapping("/members/join")
    public String join(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/join";
    }

    @GetMapping("/members/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "members/login";
    }

}
