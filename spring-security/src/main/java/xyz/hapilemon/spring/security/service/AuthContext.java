package xyz.hapilemon.spring.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.hapilemon.spring.security.pojo.Account;

@Service
public class AuthContext {

    /**
     * 将当前账号存起来
     *
     * @param authentication
     */
    public void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 将存起来的取出来
     *
     * @return
     */
    public Account getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }

}
