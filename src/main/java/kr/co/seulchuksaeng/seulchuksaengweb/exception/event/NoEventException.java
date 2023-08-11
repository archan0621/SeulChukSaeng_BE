package kr.co.seulchuksaeng.seulchuksaengweb.exception.event;

public class NoEventException extends RuntimeException {

    public NoEventException() {
        super("경기가 존재하지 않습니다.");
    }

    public NoEventException(String message) {
        super(message);
    }

    public NoEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEventException(Throwable cause) {
        super(cause);
    }

}
