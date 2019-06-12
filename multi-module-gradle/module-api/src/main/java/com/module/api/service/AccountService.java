package com.module.api.service;

import com.module.core.domain.Account;
import com.module.core.domain.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;

    public Account signUp(Account account){
        return accountRepository.save(account);
    }

}
