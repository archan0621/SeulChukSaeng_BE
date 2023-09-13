package kr.co.seulchuksaeng.seulchuksaengweb.aop;

import com.github.archan0621.DiscordLogger;
import com.github.archan0621.Scope;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
    Author : ParkJongHa
    Date   : 2023-09-01
    Desc   : Controller의 응답이 느릴 경우, Discord 알림을 보내는 Aspect
 **/

@Component
@Aspect
@Slf4j
public class SlowResponseAspect {

    private final DiscordLogger discordLogger = DiscordLogger.instance();

    @Around("@annotation(kr.co.seulchuksaeng.seulchuksaengweb.annotation.LogExecutionTime)")
    public Object slowResponseCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        long responseTime = end - start;

        if (responseTime > 500) {
            log.warn("🚨경고🚨 응답 지연 발생 - : " + joinPoint.getSignature().toShortString() + " 응답 시간 : " + responseTime + "ms");
            CompletableFuture.runAsync(() -> { // 비동기로 Discord 알림을 보냄
                discordLogger.send("🚨경고🚨 응답 지연 발생 - " + joinPoint.getSignature().toShortString() + " 응답 시간 : " + responseTime + "ms", Scope.here);
            });
        }

        return result;
    }

}
