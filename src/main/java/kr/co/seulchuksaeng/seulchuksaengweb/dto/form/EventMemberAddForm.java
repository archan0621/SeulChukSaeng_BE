package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import lombok.Getter;

@Getter
public class EventMemberAddForm {

    private String eventId;

    private String memberId;

    public EventMemberAddForm(String eventId, String memberId) {
        this.eventId = eventId;
        this.memberId = memberId;
    }

}
