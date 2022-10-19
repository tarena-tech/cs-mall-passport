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

import com.tarena.passport.common.pojo.model.User;
import com.tarena.passport.doman.repository.UserRepository;
import com.tarena.passport.instruction.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositry implements UserRepository {
    @Autowired
    private UserMapper userMapper;
    @Override
    public int addNewUser(User user) {
        return userMapper.addNewUser(user);
    }

    @Override public User getUserByUsername(String username) {
        return userMapper.getUserByUserName(username);
    }

    @Override public User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override public User getUserByMail(String email) {
        return userMapper.getUserByMail(email);
    }

}
