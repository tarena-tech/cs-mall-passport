package com.tarena.passport.adaptor.filter;

import com.alibaba.fastjson.JSON;
import com.tarena.passport.auto.domain.JwtRSAGenerator;
import com.tarena.passport.protocol.LoginInfo;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ResultEnum;
import com.tarena.passport.protocol.result.JsonResult;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtRSAGenerator<LoginInfo> jwtRSAGenerator;

    @SneakyThrows @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {


        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
        response.setHeader("Access-Control-Expose-Headers", "*");

        if (request.getMethod().equals("OPTIONS")) {
            return;
        }

        String uri = request.getRequestURI();
        if ("/user/login".equals(uri)) {
            chain.doFilter(request, response);
            return;
        }
        String jwt = request.getHeader("Authorization");
        try {

            if (!StringUtils.hasText(jwt) || jwt.length() < 113)
                throw new PassportBusinessException(ResultEnum.TOKEN_EXPIRES);

            LoginInfo userInfo = jwtRSAGenerator.getLoginFromToken(jwt, LoginInfo.class);

            if (userInfo == null)
                throw new PassportBusinessException(ResultEnum.TOKEN_EXPIRES);

        } catch (PassportBusinessException e) {
            response.setContentType("text/html;charset=utf-8");
            JsonResult fail = JsonResult.fail(e);
            String result = JSON.toJSONString(fail);
            PrintWriter writer = response.getWriter();
            writer.println(result);
            writer.close();
            return;
        }

        chain.doFilter(request, response);
    }
}
