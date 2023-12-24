package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class UnverifiedJoinException extends MemberException {
    public UnverifiedJoinException() {
        super("인증된 회원이 아닙니다. 회장에게 회원코드를 요청해주세요.");
    }

    public UnverifiedJoinException(String message) {
        super(message);
    }

    public UnverifiedJoinException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnverifiedJoinException(Throwable cause) {
        super(cause);
    }
}
