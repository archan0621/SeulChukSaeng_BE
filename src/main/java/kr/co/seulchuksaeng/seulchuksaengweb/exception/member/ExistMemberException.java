package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class ExistMemberException extends MemberException {
    public ExistMemberException() {
        super("이미 존재하는 회원입니다.");
    }

    public ExistMemberException(String message) {
        super(message);
    }

    public ExistMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistMemberException(Throwable cause) {
        super(cause);
    }
}
