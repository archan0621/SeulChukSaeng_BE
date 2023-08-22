package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import lombok.Getter;

@Getter
public class GetUserNameResult {

    private String result;

    private String message;

    private UserRole userRole;

    public GetUserNameResult(String result, String message, UserRole userRole) {
        this.result = result;
        this.message = message;
        this.userRole = userRole;
    }

}
