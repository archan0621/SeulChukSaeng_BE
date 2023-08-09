package kr.co.seulchuksaeng.seulchuksaengweb.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')") //USER, ADMIN 모두 접근 가능
public @interface UserAuthorize {}
