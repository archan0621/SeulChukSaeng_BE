package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

public class MemberForm {

    @Getter
    public static class Join { // 회원가입 폼
        private String id;

        private String password;

        private String name;

        private String phone;

        private Gender gender;

        private String verifyCode;

        public Join(String id, String password, String name, String phone, Gender gender, String verifyCode) {
            this.id = id;
            this.password = password;
            this.name = name;
            this.phone = phone;
            this.gender = gender;
            this.verifyCode = verifyCode;
        }
    }

    @Getter
    public static class Login { //로그인 요청 받을 때 사용하는 DTO

        private String loginId;
        private String password;

        public Login(String loginId, String password) {
            this.loginId = loginId;
            this.password = password;
        }
    }


}
