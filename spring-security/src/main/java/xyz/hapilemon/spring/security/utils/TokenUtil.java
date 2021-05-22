package xyz.hapilemon.spring.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import xyz.hapilemon.spring.security.pojo.Account;
import xyz.hapilemon.spring.security.service.AuthContext;

import java.util.UUID;

@Component
public class TokenUtil {

    @Autowired
    private AuthContext authContext;

    /**
     * 产生一个token
     *
     * @param account
     * @return
     */
    public String generateToken(Account account) {
        String jti = UUID.randomUUID().toString().replace("-", "");
        String token = Jwts.builder()
                .setSubject("1")
                .claim("username", account.getUsername())
                .claim("jti", jti)
                .signWith(SignatureAlgorithm.HS256, "hahaha")
                .compact();
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     */
    public void decodeToken(String token) {
        Claims claims = Jwts.parser().setSigningKey("hahaha").parseClaimsJws(token).getBody();
        String username = (String) claims.get("username");
        Account account = new Account();
        account.setUsername(username);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(account, "", null);

        authContext.setAuthentication(usernamePasswordAuthenticationToken);
    }

}
