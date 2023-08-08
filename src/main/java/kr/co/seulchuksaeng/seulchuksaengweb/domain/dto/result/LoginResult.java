package kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result;

import lombok.Getter;

@Getter
public class LoginResult { //로그인 요청에 대한 응답 DTO

    private String result;

    private String message;

    private String token;

    public LoginResult(String result, String message, String token) {
        this.result = result;
        this.message = message;
        this.token = token;
    }

}
