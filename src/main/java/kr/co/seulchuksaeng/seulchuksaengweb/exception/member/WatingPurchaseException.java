package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class WatingPurchaseException extends MemberException {
    public WatingPurchaseException() {
        super("관리자의 납부 확인을 기다리고 있는중입니다.");
    }

    public WatingPurchaseException(String message) {
        super(message);
    }

    public WatingPurchaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public WatingPurchaseException(Throwable cause) {
        super(cause);
    }
}
