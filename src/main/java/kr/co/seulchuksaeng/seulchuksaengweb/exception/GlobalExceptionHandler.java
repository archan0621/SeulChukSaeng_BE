package kr.co.seulchuksaeng.seulchuksaengweb.exception;

import com.github.archan0621.DiscordLogger;
import com.github.archan0621.Scope;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.EventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.ExistMemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.MemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.security.ExpiredTokenException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    DiscordLogger discordLogger = DiscordLogger.instance();

    public record BasicErrorResult(String result, String message) {
    }

    //엔드포인트들의 예외를 처리하는 부분
    @ExceptionHandler({MemberException.class, EventException.class, ExpiredTokenException.class})
    protected BasicErrorResult handleEndPointException(Exception e) {
        return new BasicErrorResult("fail", e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected BasicErrorResult handleNoTokenException(Exception e) {
        return new BasicErrorResult("fail", "개발자에게 전하는 말 - 토큰과 함께 요청하세요");
    }


    @ExceptionHandler(RuntimeException.class)
    protected BasicErrorResult handleRunTimeException(Exception e) {
        discordLogger.send(String.format("🚨경고🚨 백엔드에서 예상치 못한 오류 생김 개발한 사람 나와, 오류 내용\n- %s", e.toString()), Scope.here);
        e.printStackTrace();
        return new BasicErrorResult("fail", "예상치 못한 오류 발생 관리자에게 문의하세요.");
    }

}
