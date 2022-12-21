package com.tarena.test.passport.main;

import com.tarena.passport.main.PassportApplication;
import com.tarena.passport.sdk.LoginUserArgumentResolvers;
import com.tarena.passport.sdk.filter.LoginUserFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PassportApplication.class)
public class MainTest {
    @Autowired
    private LoginUserFilter loginUserFilter;
    @Autowired
    private LoginUserArgumentResolvers loginUserArgumentResolvers;

    @Test
    public void test(){
        System.out.println(loginUserFilter.getClass().getName());
        System.out.println(loginUserArgumentResolvers.getClass().getName());
    }
}
