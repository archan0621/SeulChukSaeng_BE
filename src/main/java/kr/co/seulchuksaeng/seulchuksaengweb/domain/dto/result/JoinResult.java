package kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.result;

import lombok.Getter;

@Getter
public class JoinResult {

    private String result;

    private String message;

    public JoinResult(String result, String message) {
        this.result = result;
        this.message = message;
    }
}
