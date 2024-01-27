package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;

import java.time.LocalDateTime;

public record EventShowcaseInnerResult(
        Long eventId,
        String eventTitle,
        Gender gender,
        String location,
        String money,
        LocalDateTime startTime
) {}

