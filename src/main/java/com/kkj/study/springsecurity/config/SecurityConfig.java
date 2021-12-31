package com.kkj.study.springsecurity.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
//@Order(Ordered.LOWEST_PRECEDENCE -15)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 계층형 권한 설정 커스터마이징
    // Start
    public AccessDecisionManager accessDecisionManager() {
        // AccessDecisionManager를 커스터마이징
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(handler);
        List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(webExpressionVoter);
        return new AffirmativeBased(voters);
    }

    public SecurityExpressionHandler securityExpressionHandler() {
        // ExpressionHandler 커스터마이징
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }
    // End

    //원칙적으로는 해줘야하는데 UserDetailsService를 구현한 구현체가 Bean으로 등록되어 있으면 안해줘도 상관없다(AccountService)
//    @Autowired
//    AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //특정 url은 인증을 거치고 싶지 않다
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .mvcMatchers("/user").hasRole("USER")
                .anyRequest().authenticated() // 기타 다른 url은 인증을 해야한다.
                //.accessDecisionManager(accessDecisionManager());  // AccessDecisionManager를 커스터마이징
                .expressionHandler(securityExpressionHandler());
                // AccessDecisionManager의 voter가 사용하는 ExpressionHandler 커스터마이징
//                .and()              //and   method chaining을 반드시 하지 않아도 됨
//                .formLogin()        //Form Login 사용
//                .and()
//                .httpBasic();       //HttpBasic 사용
        http.formLogin();
        http.httpBasic();

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
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


    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().mvcMatchers("/favicon.ico");
        // 일반적인 위치에 있는 정적 리소스들은 시큐리티 필터 적용 제외
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
