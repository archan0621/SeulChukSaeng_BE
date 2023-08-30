package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import lombok.Getter;

@Getter
public class EventMemberResult {

    @Getter
    public static class basic {

        private String result;

        private String message;

        public basic(String result, String message) {
            this.result = result;
            this.message = message;
        }
    }


    private String result;

    private String message;

    public EventMemberResult(String result, String message) {
        this.result = result;
        this.message = message;
    }
}
