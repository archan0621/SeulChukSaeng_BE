package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import lombok.Getter;

@Getter
public class EventShowcaseInnerResult {

    private Long eventId;

    private String eventTitle;

    public EventShowcaseInnerResult(Long eventId, String eventTitle) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
    }

}
