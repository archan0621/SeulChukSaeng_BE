package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;

public record MemberListInnerResult(
        Long id,
        String name,
        Gender gender,
        String phone,
        int warnPoint
) {}
