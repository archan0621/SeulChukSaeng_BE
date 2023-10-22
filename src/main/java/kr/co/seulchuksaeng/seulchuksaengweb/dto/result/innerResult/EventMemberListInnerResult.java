package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Attendance;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.PurchaseStatus;
import lombok.Getter;

public record EventMemberListInnerResult(
        String memberId,
        String memberName,
        Attendance attendance,
        PurchaseStatus purchaseStatus
) {}
