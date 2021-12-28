package com.kkj.study.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //원칙적으로는 해줘야하는데 UserDetailsService를 구현한 구현체가 Bean으로 등록되어 있으면 안해줘도 상관없다(AccountService)
//    @Autowired
//    AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //특정 url은 인증을 거치고 싶지 않다
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**").permitAll()
                .mvcMatchers("/admin").hasRole(("ADMIN"))
                .anyRequest().authenticated(); // 기타 다른 url은 인증을 해야한다.
//                .and()              //and   method chaining을 반드시 하지 않아도 됨
//                .formLogin()        //Form Login 사용
//                .and()
//                .httpBasic();       //HttpBasic 사용
        http.formLogin();
        http.httpBasic();
    }
//    JPA인증을 구현 했으므로 inMemory 사용자 관련 소스는 주석 처리
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //원하는 유저 정보를 임의로 설정할 수 있는 메소드
//        auth.inMemoryAuthentication()
//                .withUser("kyeongjun").password("{noop}123").roles("USER").and()    //{noop}암호화를 하지 않는다
//                .withUser("admin").password("{noop}!@#").roles("ADMIN");
//
//    }

//      원칙적으로는 해줘야하는데 UserDetailsService를 구현한 구현체가 Bean으로 등록되어 있으면 안해줘도 상관없다(AccountService)
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(accountService);
//    }
}
