package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import lombok.Getter;

@Getter
public class EventReadForm {

    private Long eventId;

    public EventReadForm(Long eventId) {
        this.eventId = eventId;
    }

    public EventReadForm() {} // 없으면 cannot deserialize from Object value (no delegate- or property-based Creator) 에러 발생

}
