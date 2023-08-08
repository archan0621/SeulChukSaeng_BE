package kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form;

import lombok.Getter;

@Getter
public class LoginForm { //로그인 요청 받을 때 사용하는 DTO

    private String loginId;
    private String password;

    public LoginForm(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
