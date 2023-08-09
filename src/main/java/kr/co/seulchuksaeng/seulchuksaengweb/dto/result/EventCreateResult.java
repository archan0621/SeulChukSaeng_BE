package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import lombok.Getter;

@Getter
public class EventCreateResult {

    private String result;

    private String message;

    public EventCreateResult(String result, String message) {
        this.result = result;
        this.message = message;
    }

}
