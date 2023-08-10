package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventCreateForm {

    private String title;

    private String location;

    private Gender gender;

    private String money;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

    public EventCreateForm(String location, Gender gender, String money, LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.location = location;
        this.gender = gender;
        this.money = money;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

}
