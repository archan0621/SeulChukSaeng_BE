package kr.co.seulchuksaeng.seulchuksaengweb.controller.form;

import jakarta.validation.constraints.NotEmpty;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 아이디는 필수 입니다")
    private String id;

    @NotEmpty(message = "회원 비밀번호는 필수 입니다")
    private String password;

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;

    @NotEmpty(message = "회원 이메일은 필수 입니다")
    private String phone;

    @NotEmpty(message = "회원 성별은 필수 입니다")
    private Gender gender;

    @NotEmpty(message = "회원 인증코드는 필수 입니다")
    private String authCode;
}
