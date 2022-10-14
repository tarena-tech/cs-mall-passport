package com.tarena.passport.auto.test;

import com.tarena.passport.auto.domain.JwtRSAGenerator;
import com.tarena.passport.protocol.LoginInfo;
import com.tarena.passport.protocol.PassportBusinessException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AutoTestStarter.class)
@Slf4j
public class AutoTest {
    @Autowired
    private JwtRSAGenerator<LoginInfo> jwtRSAGenerator;

    @Test
    public void encodeToken() throws PassportBusinessException {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(1L);
        String jwt = jwtRSAGenerator.generateToken(loginInfo);
        System.out.println(jwt);
    }
    @Test
    public void decodeToken() throws PassportBusinessException {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(1L);
        Long start=new Date().getTime();
        String jwt = jwtRSAGenerator.generateToken(loginInfo);
        Long end=new Date().getTime();
        System.out.println("获取jwt:"+jwt+";用时["+(end-start)+"]毫秒");
        start=new Date().getTime();
        LoginInfo token = jwtRSAGenerator.getLoginFromToken(jwt, LoginInfo.class);
        end=new Date().getTime();
        System.out.println("获得用户信息id:"+token.getId()+";用时["+(end-start)+"]毫秒");
    }

}
