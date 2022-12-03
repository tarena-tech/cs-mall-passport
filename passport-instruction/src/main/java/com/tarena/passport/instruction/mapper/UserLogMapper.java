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

package com.tarena.passport.instruction.mapper;

import com.tarena.passport.common.pojo.model.OperateDetailDO;
import com.tarena.passport.common.pojo.model.UserLogDO;
import com.tarena.passport.common.pojo.param.UserOperateParam;
import com.tarena.passport.common.pojo.query.LoginLogQuery;
import com.tarena.passport.common.pojo.query.UserOperateQuery;
import java.util.List;

public interface UserLogMapper {

    int insertUserLog(UserLogDO log);

    List<LoginLogQuery> getLoginLogList(LoginLogQuery logQuery);

    int insertOperateLog(UserOperateParam param);

    List<OperateDetailDO> getList(UserOperateQuery userOperateQuery);

    int deleteLoginLogById(Long id);

    int deleteOperateLogById(Long id);
}
