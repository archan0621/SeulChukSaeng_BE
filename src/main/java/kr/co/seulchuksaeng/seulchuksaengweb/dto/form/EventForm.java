package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

import java.time.LocalDateTime;

public record EventForm() {

    public record Create (String title, String location, Gender gender, String money, LocalDateTime startTime, LocalDateTime endTime, String description) {}

    public record Read (Long eventId) {}

    public record Remove (String eventId) {}

    public record Update (String eventId, String title, String location, Gender gender, String money, LocalDateTime startTime, LocalDateTime endTime, String description) {}

}
