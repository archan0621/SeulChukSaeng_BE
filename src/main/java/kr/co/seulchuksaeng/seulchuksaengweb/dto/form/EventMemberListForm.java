package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import lombok.Getter;

@Getter
public class EventMemberListForm {

    private String eventId;

    public EventMemberListForm(String eventId) {
        this.eventId = eventId;
    }

    public EventMemberListForm() {}
}
