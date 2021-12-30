package com.kkj.study.springsecurity.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    // 코드 기반
    @Test
    public void index_anonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_user() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(user("admin").roles("USER"))) // 해당 사용자가 로그인을 했다고 가정하고
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_admin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(user("admin").roles("ADMIN"))) // 해당 사용자가 로그인을 했다고 가정하고
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void admin_user() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin").with(user("admin").roles("USER"))) // 해당 사용자가 로그인을 했다고 가정하고
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void admin_admin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin").with(user("admin").roles("ADMIN"))) // 해당 사용자가 로그인을 했다고 가정하고
                .andDo(print())
                .andExpect(status().isOk());
    }

    //---------------------------------------
    // 코드 기반 End

    // Annotation 기반
    @Test
    @WithAnonymousUser
    public void index_anonymous_annotation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    public void index_user_annotation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void admin_admin_annotation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    public void admin_user_annotation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithUser
    public void admin_user_customAnnotation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // Annotation 기반 End

    private Account createUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");
        return accountService.createNew(account);
    }

    @Test
    @Transactional
    public void login_success() throws Exception {
        String username = "admin";
        String password = "123";
        Account account = createUser(username, password);
        mockMvc.perform(formLogin().user(account.getUsername()).password(password))
                .andExpect(authenticated());
    }
    @Test
    @Transactional
    public void login_fail() throws Exception {
        String username = "admin";
        String password = "123";
        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(unauthenticated());
    }
}
