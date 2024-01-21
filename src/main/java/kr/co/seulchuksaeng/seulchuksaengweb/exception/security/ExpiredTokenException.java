package kr.co.seulchuksaeng.seulchuksaengweb.exception.security;

public class ExpiredTokenException extends SecurityException {
    public ExpiredTokenException() {
        super("만료된 토큰으로 JWT 요청시도");
    }

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredTokenException(Throwable cause) {
        super(cause);
    }
}
