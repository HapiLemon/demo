package xyz.hapilemon.spring.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.hapilemon.spring.security.pojo.Account;
import xyz.hapilemon.spring.security.utils.TokenUtil;
import xyz.hapilemon.spring.security.vo.AuthVO;
import xyz.hapilemon.spring.security.vo.LoginVO;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthContext authContext;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 登录
     *
     * @param loginVO
     * @return
     */
    public AuthVO login(LoginVO loginVO) {

        // 进行登录账号密码的校验
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginVO.getUsername(), loginVO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // 将account存起来
        authContext.setAuthentication(authenticate);
        Account account = new Account();
        account.setUsername(loginVO.getUsername());
        account.setPassword(loginVO.getPassword());
        String token = tokenUtil.generateToken(account);
        AuthVO authVO = new AuthVO();
        authVO.setToken(token);
        return authVO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中找出username 对应的account
        return this.findByUsername(username);
    }

    private Account findByUsername(String username) {

        Account account = new Account();
        account.setUsername(username);
        String password = bCryptPasswordEncoder.encode("123456");
        account.setPassword(password);
        account.setEmail("123@c.c");
        account.setPhoneNumber("11111111111");
        return account;
    }

    public Account getMe() {
        return authContext.getMe();
    }
}
