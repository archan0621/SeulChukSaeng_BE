package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import lombok.Getter;

@Getter
public class JoinResult { //회원가입 요청에 대한 응답 DTO

    private String result;

    private String message;

    public JoinResult(String result, String message) {
        this.result = result;
        this.message = message;
    }
}
