package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import lombok.Getter;

import java.util.List;

public class MemberEventResult {

    @Getter
    public static class Add {
        private String result;

        private String message;

        public Add(String result, String message) {
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
    }

    @Getter
    public static class Attend {
        private String result;

        private String message;

        public Attend(String result, String message) {
            this.result = result;
            this.message = message;
        }
    }

    @Getter
    public static class Lists {

        private String result;

        private String message;

        private List<EventMemberListInnerResult> memberList;

        private int memberCount;

        public Lists(String result, String message, List<EventMemberListInnerResult> memberList, int memberCount) {
            this.result = result;
            this.message = message;
            this.memberList = memberList;
            this.memberCount = memberCount;
        }

    }

    @Getter
    public static class PurchaseReq {
        private String result;

        private String message;

        public PurchaseReq(String result, String message) {
            this.result = result;
            this.message = message;
        }
    }

    @Getter
    public static class PurchaseList {

        private String result;

        private String message;

        private List<EventMemberListInnerResult> memberList;

        private int memberCount;

        public PurchaseList(String result, String message, List<EventMemberListInnerResult> memberList, int memberCount) {
            this.result = result;
            this.message = message;
            this.memberList = memberList;
            this.memberCount = memberCount;
        }
    }

    @Getter
    public static class PurchaseCheck {
        private String result;

        private String message;

        public PurchaseCheck(String result, String message) {
            this.result = result;
            this.message = message;
        }
    }

}
