package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventShowcaseInnerResult;
import lombok.Getter;

import java.util.List;

@Getter
public class EventShowcaseResult {

    private String result;

    private String message;

    private List<EventShowcaseInnerResult> eventShowcase;

    private int showcaseCount;

    public EventShowcaseResult(String result, String message, List<EventShowcaseInnerResult> eventShowcase, int showcaseCount) {
        this.result = result;
        this.message = message;
        this.eventShowcase = eventShowcase;
        this.showcaseCount = showcaseCount;
    }
}
