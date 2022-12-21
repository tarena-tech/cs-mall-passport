/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tarena.passport.sdk.filter;

import com.tarena.passport.common.json.Json;
import com.tarena.passport.protocol.LoginUser;
import com.tarena.passport.protocol.constant.Constant;
import com.tarena.passport.sdk.context.SecurityContext;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUserFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(LoginUserFilter.class);

    public void setJson(Json json) {
        this.json = json;
    }

    private Json json;

    @Override public void init(FilterConfig config) throws ServletException {
        logger.info(" loginUserFilter inits");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String loginTokenOfHeader = req.getHeader(Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN);
            LoginUser loginUser = this.json.parse(loginTokenOfHeader, LoginUser.class);
            SecurityContext.bindLoginToken(loginUser);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override public void destroy() {
        logger.info(" loginUserFilter destroies");
    }
}
