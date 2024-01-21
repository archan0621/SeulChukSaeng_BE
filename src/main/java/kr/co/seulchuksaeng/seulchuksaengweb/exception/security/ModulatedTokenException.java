package kr.co.seulchuksaeng.seulchuksaengweb.exception.security;

public class ModulatedTokenException extends SecurityException {

    public ModulatedTokenException() {
        super("유효하지 않은 토큰으로 JWT 요청시도");
    }
    public ModulatedTokenException(String message) {
        super(message);
    }

    public ModulatedTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModulatedTokenException(Throwable cause) {
        super(cause);
    }
}
