package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class AlreadyAttendException extends RuntimeException {
    public AlreadyAttendException() {
        super("이미 출석처리가 완료된 경기입니다.");
    }

    public AlreadyAttendException(String message) {
        super(message);
    }

    public AlreadyAttendException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyAttendException(Throwable cause) {
        super(cause);
    }
}
