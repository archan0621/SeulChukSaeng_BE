package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class NoEventMemberException extends MemberException {
    public NoEventMemberException() {
        super("경기에 참여한 회원이 아닙니다.");
    }

    public NoEventMemberException(String message) {
        super(message);
    }

    public NoEventMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEventMemberException(Throwable cause) {
        super(cause);
    }
}
