package kr.co.seulchuksaeng.seulchuksaengweb.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.NetworkError;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.security.ExpiredTokenException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.security.ModulatedTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 권한이 없는 Jwt로 요청이 오면 경고 로그를 출력하고 에러메세지를 돌려준다
        String reqToken = jwtAuthenticationFilter.parseBearerToken(request);
        User user = null;
        try {
            user = jwtAuthenticationFilter.parseUserSpecification(reqToken);
        } catch (ExpiredTokenException e) {
            sendError(response, HttpStatus.FORBIDDEN, new NetworkError("fail", "만료된 토큰으로 JWT 요청시도"));
        } catch (ModulatedTokenException e) {
            sendError(response, HttpStatus.FORBIDDEN, new NetworkError("fail", "유효하지 않은 토큰으로 JWT 요청시도"));
        }

        log.warn("권한 없는 접근 시도 - 아이디: {}, 아이피: {}, URL: {}", user != null ? user.getUsername() : "Unknown", request.getRemoteAddr(), request.getRequestURI());
        sendError(response, HttpStatus.FORBIDDEN, new NetworkError("fail", "접근 권한이 없습니다"));
    }

    private void sendError(HttpServletResponse response, HttpStatus status, NetworkError errorMessage) throws IOException { // HTTP 에러 메세지 보내주는 메서드
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8"); // 문자 인코딩을 UTF-8로 설정
        response.setContentType("application/json"); // JSON 형식으로 데이터를 처리
        response.getWriter().write(errorMessage.toString());
    }
}
