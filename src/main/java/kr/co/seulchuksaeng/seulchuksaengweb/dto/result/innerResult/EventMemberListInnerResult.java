package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import lombok.Getter;

@Getter
public class EventMemberListInnerResult {

    private String memberId;

    private String memberName;

    public EventMemberListInnerResult(String memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }

}
