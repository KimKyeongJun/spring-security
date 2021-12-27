package com.kkj.study.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //특정 url은 인증을 거치고 싶지 않다
        http.authorizeRequests()
                .mvcMatchers("/", "/info").permitAll()
                .mvcMatchers("/admin").hasRole(("ADMIN"))
                .anyRequest().authenticated(); // 기타 다른 url은 인증을 해야한다.
//                .and()              //and   method chaining을 반드시 하지 않아도 됨
//                .formLogin()        //Form Login 사용
//                .and()
//                .httpBasic();       //HttpBasic 사용
        http.formLogin();
        http.httpBasic();
    }
}