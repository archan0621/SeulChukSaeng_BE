package kr.co.seulchuksaeng.seulchuksaengweb.exception;

public class NetworkException extends RuntimeException {
    public NetworkException() { super("네트워크 연결에 실패하였습니다."); }
}
