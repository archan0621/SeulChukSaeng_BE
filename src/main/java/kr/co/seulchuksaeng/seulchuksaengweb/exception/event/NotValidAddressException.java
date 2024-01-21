package kr.co.seulchuksaeng.seulchuksaengweb.exception.event;

public class NotValidAddressException extends EventException {
    public NotValidAddressException() { super("유효하지 않은 주소입니다."); }
}