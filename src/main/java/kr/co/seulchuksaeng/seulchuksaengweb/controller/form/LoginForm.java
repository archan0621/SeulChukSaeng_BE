package kr.co.seulchuksaeng.seulchuksaengweb.controller.form;

import jakarta.validation.constraints.NotEmpty;

public class LoginForm {

    @NotEmpty(message = "회원 아이디는 필수 입니다")
    private String id;

    @NotEmpty(message = "회원 비밀번호는 필수 입니다")
    private String password;

}
