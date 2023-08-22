package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventUpdateForm {

    private String eventId;

    private String title;

    private String location;

    private Gender gender;

    private String money;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

}
