package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super("존재하지 않는 회원입니다.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

}
