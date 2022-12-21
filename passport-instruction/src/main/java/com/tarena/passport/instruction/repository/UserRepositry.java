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

package com.tarena.passport.instruction.repository;

import com.tarena.passport.common.pojo.model.LogDetailDO;
import com.tarena.passport.common.pojo.model.UserDO;
import com.tarena.passport.common.pojo.model.UserLogDO;
import com.tarena.passport.common.pojo.query.UserQuery;
import com.tarena.passport.doman.repository.UserRepository;
import com.tarena.passport.instruction.mapper.UserLogMapper;
import com.tarena.passport.instruction.mapper.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositry implements UserRepository {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public int addNewUser(UserDO userDO) {
        return userMapper.addNewUser(userDO);
    }

    @Override public UserDO getUserByUsername(String username) {
        return userMapper.getUserByUserName(username);
    }

    @Override public UserDO getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override public UserDO getUserByMail(String email) {
        return userMapper.getUserByMail(email);
    }

    @Override public int insertUserLog(UserLogDO log) {
        return userLogMapper.insertUserLog(log);
    }

    @Override public UserDO getUserByUserID(Long id) {
        return userMapper.getUserByUserID(id);
    }

    @Override public List<UserDO> getUserList(UserQuery query) {
        List<UserDO> userList = userMapper.getUserList(query);
        return userList;
    }

    @Override public void deleteUserById(Long id) {
        userMapper.deleteById(id);
    }

    @Override public UserDO selectUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    @Override public int updateUser(UserDO user) {
        return userMapper.updateUser(user);
    }

    @Override public int insertUserLogDetail(LogDetailDO detail) {
        return userMapper.insertUserLogDetail(detail);
    }

}
