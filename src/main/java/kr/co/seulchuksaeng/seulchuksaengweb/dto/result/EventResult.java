package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventReadInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventShowcaseInnerResult;
import lombok.Getter;

import java.util.List;

public class EventResult {

    @Getter
    public static class Create {

        private String result;

        private String message;

        public Create(String result, String message) {
            this.result = result;
            this.message = message;
        }
    }

    @Getter
    public static class Read {

        private String result;

        private String message;

        private EventReadInnerResult readResult;

        public Read(String result, String message, EventReadInnerResult readResult) {
            this.result = result;
            this.message = message;
            this.readResult = readResult;
        }
    }

    @Getter
    public static class Update {

        private String result;

        private String message;

        public Update(String result, String message) {
            this.result = result;
            this.message = message;
        }
    }

    @Getter
    public static class Remove {

        private String result;

        private String message;

        public Remove(String result, String message) {
            this.result = result;
            this.message = message;
        }

        public Remove() {}
    }

    @Getter
    public static class Showcase {

        private String result;

        private String message;

        private List<EventShowcaseInnerResult> eventShowcase;

        private int showcaseCount;

        public Showcase(String result, String message, List<EventShowcaseInnerResult> eventShowcase, int showcaseCount) {
            this.result = result;
            this.message = message;
            this.eventShowcase = eventShowcase;
            this.showcaseCount = showcaseCount;
        }
    }

}
