package com.module.fo.controller;

import com.module.core.domain.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoController {
    @GetMapping("/")
    public Account signUp(){
        Account account = Account.builder()
                .userName("user1")
                .password("password")
                .build();

        return account;
    }
}
