package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

public record MemberEventForm() {

    public record Attend (String eventId, String lat, String lng) {}

    public record Add(String eventId, String memberId) {}

    public record Remove(String eventId, String memberId) {}

    public record PurchaseReq(String eventId, String memberId) {}

    public record PurchaseList(String eventId, String memberId) {}

    public record PurchaseCheck(String eventId, String memberId) {}

    public record List(String eventId, Gender gender) {
        public List {}

    }

}
