package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import lombok.Getter;

@Getter
public class EventAttendResult {

    private String result;

    private String message;

    public EventAttendResult(String result, String message) {
        this.result = result;
        this.message = message;
    }
}
