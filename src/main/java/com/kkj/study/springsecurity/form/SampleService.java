package com.kkj.study.springsecurity.form;

import com.kkj.study.springsecurity.account.Account;
import com.kkj.study.springsecurity.account.AccountContext;
import com.kkj.study.springsecurity.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public void dashboard() {
//        Authentication 객체 확인 Debugging Source
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Object credentials = authentication.getCredentials();
//        boolean authenticated = authentication.isAuthenticated();
//

        Account account = AccountContext.getAccount();
        System.out.println("==================");
        System.out.println(account.getUsername());

    }

    @Async
    public void asyncService() {
        SecurityLogger.log("Async Service");
        System.out.println("Async service is called");
    }
}
