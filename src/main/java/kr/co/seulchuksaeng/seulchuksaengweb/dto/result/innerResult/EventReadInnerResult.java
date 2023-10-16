package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
public record EventReadInnerResult(
        Long eventId,
        String title,
        Gender gender,
        String money,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location
) {}
