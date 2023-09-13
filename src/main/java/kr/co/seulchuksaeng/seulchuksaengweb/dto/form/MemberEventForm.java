package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

public class MemberEventForm {

    @Getter
    public static class Attend {
        private String eventId;
        private String lat;
        private String lng;
    }

    @Getter
    public static class Add {
        private String eventId;
        private String memberId;

        public Add(String eventId, String memberId) {
            this.eventId = eventId;
            this.memberId = memberId;
        }
    }

    @Getter
    public static class Remove {
        private String eventId;
        private String memberId;

        public Remove(String eventId, String memberId) {
            this.eventId = eventId;
            this.memberId = memberId;
        }
    }

    @Getter
    public static class PurchaseReq {
        private String eventId;
        private String memberId;

        public PurchaseReq(String eventId, String memberId) {
            this.eventId = eventId;
            this.memberId = memberId;
        }
    }

    @Getter
    public static class PurchaseList {
        private String eventId;
        private String memberId;

        public PurchaseList(String eventId, String memberId) {
            this.eventId = eventId;
            this.memberId = memberId;
        }
    }

    @Getter
    public static class PurchaseCheck {
        private String eventId;
        private String memberId;

        public PurchaseCheck(String eventId, String memberId) {
            this.eventId = eventId;
            this.memberId = memberId;
        }
    }

    @Getter
    public static class List {

        private String eventId;

        public List(String eventId, Gender gender) {
            this.eventId = eventId;
        }

        public List() {}
    }

}
