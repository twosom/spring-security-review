package com.icloud.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        createNewAccount("hope", "123", "ADMIN");
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    private void createNewAccount(String username, String password, String role) {
        Account newAccount = Account.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        accountService.createNew(newAccount);
    }

    //    @WithUserDetails(value = "hope", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @WithAnonymousUser
    @Test
    void index_anonymous() throws Exception {
        mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }


    @Test
    @WithUser
    void index_user() throws Exception {
        mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }


    @Test
    @WithUser
    void admin_user() throws Exception {
        mockMvc
                .perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @WithMockUser(username = "test_user", roles = "ADMIN")
    void admin_admin() throws Exception {
        mockMvc
                .perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    void login_success() throws Exception {
        String username = "hope";
        String password = "123";
        mockMvc.perform(formLogin("/login")
                        .user(username)
                        .password(password))
                .andDo(print())
                .andExpect(authenticated())
        ;
    }

    @Test
    void login_failed() throws Exception {
        String username = "hope";
        String password = "12345";
        mockMvc.perform(formLogin("/login")
                        .user(username)
                        .password(password))
                .andDo(print())
                .andExpect(unauthenticated())
        ;
    }

}