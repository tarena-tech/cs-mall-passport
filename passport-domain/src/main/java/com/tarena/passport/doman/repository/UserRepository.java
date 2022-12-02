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

package com.tarena.passport.doman.repository;

import com.tarena.passport.common.pojo.model.LogDetailDO;
import com.tarena.passport.common.pojo.model.UserDO;
import com.tarena.passport.common.pojo.model.UserLogDO;
import com.tarena.passport.common.pojo.param.UserParam;
import com.tarena.passport.common.pojo.query.UserQuery;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    int addNewUser(UserDO userDO);

    UserDO getUserByUsername(String username);

    UserDO getUserByPhone(String phone);

    UserDO getUserByMail(String email);

    int insertUserLog(UserLogDO log);

    UserDO getUserByUserID(Long id);

    List<UserDO> getUserList(UserQuery query);

    void deleteUserById(Long id);

    UserDO selectUserById(Long id);

    int updateUser(UserDO user);

    int insertUserLogDetail(LogDetailDO detail);
}
