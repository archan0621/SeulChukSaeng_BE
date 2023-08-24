package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import lombok.Getter;

@Getter
public class EventRemoveResult {

    private String result;

    private String message;

    public EventRemoveResult(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public EventRemoveResult() {}
}
