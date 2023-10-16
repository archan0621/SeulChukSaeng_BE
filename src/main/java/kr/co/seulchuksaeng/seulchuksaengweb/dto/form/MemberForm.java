package kr.co.seulchuksaeng.seulchuksaengweb.dto.form;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import lombok.Getter;

public record MemberForm() {

    public record Join(String id, String password, String name, String phone, Gender gender, String verifyCode) {}

    public record Login (String loginId, String password) {}

}
