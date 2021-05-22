package xyz.hapilemon.spring.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.hapilemon.spring.security.pojo.Account;
import xyz.hapilemon.spring.security.service.AccountService;
import xyz.hapilemon.spring.security.vo.AuthVO;
import xyz.hapilemon.spring.security.vo.LoginVO;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public AuthVO login(@RequestBody LoginVO loginVO) {
        return accountService.login(loginVO);
    }

    @GetMapping("/api/get")
    public Account getAll() {
        Account account = new Account();
        account.setUsername("aaaaaa");
        return account;
    }

    @GetMapping("/api/me")
    public Account getMe() {
        return accountService.getMe();
    }
}
