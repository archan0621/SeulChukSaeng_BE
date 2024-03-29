package kr.co.seulchuksaeng.seulchuksaengweb.exception.security;

import kr.co.seulchuksaeng.seulchuksaengweb.exception.SeulChukSaengException;

public abstract class SecurityException extends SeulChukSaengException {
    public SecurityException() {
        super();
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    protected SecurityException(String message, Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
