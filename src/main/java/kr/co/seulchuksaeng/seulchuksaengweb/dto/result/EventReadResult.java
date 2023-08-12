package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;


import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventReadInnerResult;
import lombok.Getter;

@Getter
public class EventReadResult {

    private String result;

    private String message;

    private EventReadInnerResult readResult;

    public EventReadResult(String result, String message, EventReadInnerResult readResult) {
        this.result = result;
        this.message = message;
        this.readResult = readResult;
    }

}
