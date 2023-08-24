package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import lombok.Getter;

@Getter
public class EventRemoveForm {

    private String eventId;

    public EventRemoveForm(String eventId) {
        this.eventId = eventId;
    }

    public EventRemoveForm() {}
}
