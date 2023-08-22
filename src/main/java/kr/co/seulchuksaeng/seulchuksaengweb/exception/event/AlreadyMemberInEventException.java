package kr.co.seulchuksaeng.seulchuksaengweb.exception.event;

public class AlreadyMemberInEventException extends RuntimeException {

    public AlreadyMemberInEventException() {
        super("이미 경기에 포함된 회원입니다.");
    }

    public AlreadyMemberInEventException(String message) {
        super(message);
    }

    public AlreadyMemberInEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyMemberInEventException(Throwable cause) {
        super(cause);
    }
}
