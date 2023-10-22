package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import lombok.Getter;

import java.util.List;

public record MemberEventResult() {

    public record Add(String result, String message) {}

    public record Remove(String result, String message) {}

    public record Attend(String result, String message) {}

    public record Lists(String result, String message, List<EventMemberListInnerResult> memberList, int memberCount) {}

    public record PurchaseReq(String result, String message) {}

    public record PurchaseList(String result, String message, List<EventMemberListInnerResult> memberList, int memberCount) {}

    public record PurchaseCheck(String result, String message) {}
}
