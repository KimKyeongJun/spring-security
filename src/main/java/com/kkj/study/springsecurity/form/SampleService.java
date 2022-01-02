package com.kkj.study.springsecurity.form;

import com.kkj.study.springsecurity.account.Account;
import com.kkj.study.springsecurity.account.AccountContext;
import com.kkj.study.springsecurity.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;

@Service
public class SampleService {

    @Secured("ROLE_USER")
    @RolesAllowed("ROLE_USER")
    @PreAuthorize("hasRole('USER')")
    public void dashboard() {
//        Authentication 객체 확인 Debugging Source
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Object credentials = authentication.getCredentials();
//        boolean authenticated = authentication.isAuthenticated();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("====================");
        System.out.println(authentication);
        System.out.println(userDetails.getUsername());
//

//        Account account = AccountContext.getAccount();
//        System.out.println("==================");
//        System.out.println(account.getUsername());

    }

    @Async
    public void asyncService() {
        SecurityLogger.log("Async Service");
        System.out.println("Async service is called");
    }
}
