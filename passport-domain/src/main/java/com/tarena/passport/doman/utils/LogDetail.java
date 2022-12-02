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

package com.tarena.passport.doman.utils;

import javax.servlet.http.HttpServletRequest;

public class LogDetail {

    public static String getLogDetail (HttpServletRequest httpServletRequest){
        String detail="";
        String requestURI = httpServletRequest.getRequestURI();
        if (requestURI.contains("/user/login"))     detail="用户登录";

        if (requestURI.contains("/user/add-user"))  detail="用户新增";

        if (requestURI.contains("/user/user-list")) detail="查看用户";

        if (requestURI.contains("/user/deleteById")) detail="删除用户";

        if (requestURI.contains("/enable")||requestURI.contains("/disable")) detail="状态修改";

        if (requestURI.contains("/user/update")) detail="用户修改";
        return detail;
    }

}
