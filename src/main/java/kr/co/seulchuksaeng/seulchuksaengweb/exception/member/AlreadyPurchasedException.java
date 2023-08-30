package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class AlreadyPurchasedException extends RuntimeException {
    public AlreadyPurchasedException() {
        super("이미 활동비 납부 처리가 완료된 경기입니다.");
    }

    public AlreadyPurchasedException(String message) {
        super(message);
    }

    public AlreadyPurchasedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyPurchasedException(Throwable cause) {
        super(cause);
    }
}
