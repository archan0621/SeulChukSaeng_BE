package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import lombok.Getter;

public class MemberResult {

    @Getter
    public static class Join { //회원가입 요청에 대한 응답 DTO

        private String result;

        private String message;

        public Join(String result, String message) {
            this.result = result;
            this.message = message;
        }
    }

    @Getter
    public static class Login { //로그인 요청에 대한 응답 DTO

        private String result;

        private String message;

        private String token;

        public Login(String result, String message, String token) {
            this.result = result;
            this.message = message;
            this.token = token;
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
    public static class GetUserName {

        private String result;

        private String message;

        private UserRole userRole;

        public GetUserName(String result, String message, UserRole userRole) {
            this.result = result;
            this.message = message;
            this.userRole = userRole;
        }
    }

}
