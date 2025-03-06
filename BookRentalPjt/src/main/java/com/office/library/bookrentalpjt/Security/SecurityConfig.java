package com.office.library.bookrentalpjt.Security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@AllArgsConstructor
@Configuration
public class SecurityConfig {
    @Bean // 반환되는 객체를 스프링 빈에 등록한다.
    // PasswordEncoder : 비밀번호를 암호화하고 비교하기 위한 인터페이스
    // BCryptPaswordEncoder : PasswordEncoder의 구현체
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 스프링 컨텍스트에 PasswordEncoder 타입의 빈으로 BCryptPasswordEncoder를 등록한다.
    // 즉 PasswordEncoder가 필요한 곳에 BCryptPasswordEncoder가 자동으로 주입된다.
    // 객체를 빈으로 등록하여 Singleton 방식으로 접근하므로 빈으로 등록되어있는 BCryptPasswordEncoder를 사용할 수 있다.


    @Bean // 메서드 반환값을 스프링 빈으로 등록
    // SecurityFilterChain : HTTP 요청에대한 보안 규칙(필터체인)을 정의
    // HttpSecurity : 요청 경로, 인증, 인가 정책 등을 설정하는 객체
    // 이 메서드에서 HTTP 요청 보안 설정을 정의한다.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // CSRF 보호 비활성화
                .authorizeRequests() // HTTP 요청 보안 규칙 정의
                .anyRequest().permitAll();
                // 모든 요청에 대해 인증이나 권한 없이 허용
                // 인증, 인가, 권한 체크가 필요없는 초기 개발 단계이므로 모든 요청에 대해 접근을 허용한다.
        return http.build(); // 설정이 완료된 HttpSecurity 객체를 필터 체인으로 변환하여 반환
    }
}