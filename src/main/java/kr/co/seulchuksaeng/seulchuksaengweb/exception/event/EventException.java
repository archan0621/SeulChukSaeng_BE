package kr.co.seulchuksaeng.seulchuksaengweb.exception.event;

import kr.co.seulchuksaeng.seulchuksaengweb.exception.SeulChukSaengException;

public abstract class EventException extends SeulChukSaengException {
    public EventException() {
        super();
    }

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }

    protected EventException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
