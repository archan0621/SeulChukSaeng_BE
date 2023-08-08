package kr.co.seulchuksaeng.seulchuksaengweb.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Order(0)
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);
            User user = parseUserSpecification(token);
            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
            authenticated.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage());
        }
    }

    private String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    private User parseUserSpecification(String token) throws IllegalAccessException {
        try {
            String[] split = Optional.ofNullable(token)
                    .filter(subject -> subject.length() >= 10)
                    .map(tokenProvider::validateTokenAndGetSubject)
                    .orElse("anonymous:anonymous")
                    .split(":");
            return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 토큰으로 JWT 요청시도"); //JWT 만료시 예외 처리
        } catch (JwtException e) {
            throw new IllegalAccessException("유효하지 않은 토큰으로 JWT 요청시도"); //변조된 JWT로 요청시 경고 처리
        }
    }
}
