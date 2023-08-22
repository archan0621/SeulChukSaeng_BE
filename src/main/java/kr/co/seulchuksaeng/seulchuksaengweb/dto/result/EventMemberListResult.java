package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EventMemberListResult {

    private String result;

    private String message;

    private List<EventMemberListInnerResult> memberList;

    private int memberCount;

    public EventMemberListResult(String result, String message, List<EventMemberListInnerResult> memberList, int memberCount) {
        this.result = result;
        this.message = message;
        this.memberList = memberList;
        this.memberCount = memberCount;
    }

}
