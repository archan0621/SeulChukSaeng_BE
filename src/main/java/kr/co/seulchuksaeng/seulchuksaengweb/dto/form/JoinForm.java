package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

@Getter
public class JoinForm { //회원가입 요청 받을 때 사용하는 DTO

    private String id;

    private String password;

    private String name;

    private String phone;

    private Gender gender;

    private String verifyCode;

    public JoinForm(String id, String password, String name, String phone, Gender gender, String verifyCode) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.verifyCode = verifyCode;
    }

}
