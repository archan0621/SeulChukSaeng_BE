package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import lombok.Getter;

@Getter
public class EventMemberForm {

    @Getter
    public static class basic {
        private String eventId;
        private String memberId;

        public basic(String eventId, String memberId) {
            this.eventId = eventId;
            this.memberId = memberId;
        }
    }

}
