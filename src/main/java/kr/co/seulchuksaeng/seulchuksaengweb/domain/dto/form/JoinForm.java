package kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

@Getter
public class JoinForm {

    private String id;

    private String password;

    private String name;

    private String phone;

    private Gender gender;

    public JoinForm(String id, String password, String name, String phone, Gender gender) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

}
