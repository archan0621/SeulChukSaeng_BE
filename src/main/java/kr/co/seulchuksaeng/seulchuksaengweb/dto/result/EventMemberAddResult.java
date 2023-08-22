package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import lombok.Getter;

@Getter
public class EventMemberAddResult {

    private String result;

    private String message;

    public EventMemberAddResult(String result, String message) {
        this.result = result;
        this.message = message;
    }

}
