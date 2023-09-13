package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class FarFromLocationException extends RuntimeException {
    public FarFromLocationException() {
        super("지정된 경기장에서만 출석처리가 가능합니다");
    }

}
