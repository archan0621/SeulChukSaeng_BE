package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

import kr.co.seulchuksaeng.seulchuksaengweb.exception.SeulChukSaengException;

public abstract class MemberException extends SeulChukSaengException {
    public MemberException() {
        super();
    }

    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberException(Throwable cause) {
        super(cause);
    }

    protected MemberException(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
