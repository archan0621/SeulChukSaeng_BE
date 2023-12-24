package kr.co.seulchuksaeng.seulchuksaengweb.exception;

public abstract class SeulChukSaengException extends RuntimeException {

    public String srcAddr = "";
    public String methodName = "";

    public SeulChukSaengException() {
        super();
    }

    public SeulChukSaengException(String message) {
        super(message);
    }

    public SeulChukSaengException(String srcAddr, String methodName) {
        super();
        this.srcAddr = srcAddr;
        this.methodName = methodName;
    }

    public SeulChukSaengException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeulChukSaengException(Throwable cause) {
        super(cause);
    }

    protected SeulChukSaengException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
