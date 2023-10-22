package kr.co.seulchuksaeng.seulchuksaengweb.exception.event;

public class EventAlreadyEndedException extends RuntimeException {
    public EventAlreadyEndedException() {
        super("이미 종료된 경기입니다.");
    }
}
