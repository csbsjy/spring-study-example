package com.module.api.service;

import com.module.core.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    public void signUp() {
        //given
        Account account = Account.builder()
                                .userName("user1")
                                .password("1234")
                                .build();
        //when
        Account result = accountService.signUp(account);

        //then
        assertThat(result, is(account));
    }
}