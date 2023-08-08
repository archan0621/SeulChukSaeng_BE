package kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result;

import lombok.Getter;

@Getter
public class GetUserNameResult {

    private String result;

    private String message;

    public GetUserNameResult(String result, String message) {
        this.result = result;
        this.message = message;
    }
}
