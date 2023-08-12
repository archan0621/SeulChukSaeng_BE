package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventReadInnerResult {

    private Long eventId;

    private String title;

    private Gender gender;

    private String money;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

}
