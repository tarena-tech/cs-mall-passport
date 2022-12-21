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

import com.alibaba.fastjson.JSON;
import com.tarena.passport.common.pojo.model.UserDO;
import com.tarena.passport.common.pojo.param.UserOperateParam;
import com.tarena.passport.common.pojo.param.UserParam;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;

public class OperateDetail {

    public static <T> UserOperateParam KeepOperateDetail(UserDO userDO, T t, HttpServletRequest request,
        String operateMethod) {
        UserOperateParam param = new UserOperateParam();
        if (t instanceof UserDO)
            ((UserDO) t).setPassword("{protected}");
        if (t instanceof UserParam)
            ((UserParam) t).setPassword("{protected}");
        param.setAdminId(userDO.getId())
            .setRequestParameter(JSON.toJSON(t).toString())
            .setGmtOperate(LocalDateTime.now())
            .setState(1)
            .setDetail(LogDetail.getLogDetail(request))
            .setOperateMethod(operateMethod);

        return param;
    }
}
