package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import lombok.Getter;

public record MemberResult() {

    public record Join(String result, String message) {}

    public record Login(String result, String message, String token) {}

    public record Remove(String result, String message) {}

    public record GetUserName(String result, String message, UserRole userRole) {}

}
