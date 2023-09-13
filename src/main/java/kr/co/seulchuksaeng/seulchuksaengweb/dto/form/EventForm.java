package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

import java.time.LocalDateTime;

public class EventForm {

    @Getter
    public static class Create {

        private String title;

        private String location;

        private Gender gender;

        private String money;

        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private String description;

    }

    @Getter
    public static class Read {

        private Long eventId;

        public Read(Long eventId) {
            this.eventId = eventId;
        }

        public Read() {} // 없으면 cannot deserialize from Object value (no delegate- or property-based Creator) 에러 발생

    }

    @Getter
    public static class Remove {

        private String eventId;

        public Remove(String eventId) {
            this.eventId = eventId;
        }

        public Remove() {}
    }

    @Getter
    public static class Update {

        private String eventId;

        private String title;

        private String location;

        private Gender gender;

        private String money;

        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private String description;

    }


}
