package kr.co.seulchuksaeng.seulchuksaengweb.exception;

import com.github.archan0621.DiscordLogger;
import com.github.archan0621.Scope;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    DiscordLogger discordLogger = DiscordLogger.instance();

    public record BasicErrorResult(String result, String message) {}

    @ExceptionHandler(RuntimeException.class)
    protected BasicErrorResult handleRuntimeException(RuntimeException e) {
        return new BasicErrorResult("fail", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected BasicErrorResult handleUnExpectedException(Exception e) {
        discordLogger.send(String.format("🚨경고🚨 예상되지 않은 오류 발생! 오류 정보 : %s", e.getMessage()),Scope.here);
        return new BasicErrorResult("fail", "예상치 못한 오류가 발생했습니다, 관리자에게 제보해주세요");
    }

}
