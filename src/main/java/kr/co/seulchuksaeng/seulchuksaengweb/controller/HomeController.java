package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home(HttpServletRequest request) {
        String ip = request.getRemoteHost();

        log.info(ip + " 주소에서 사용자가 접속하였습니다!");
        return "home/home";
    }


}
