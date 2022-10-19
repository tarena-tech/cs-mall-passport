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

package com.tarena.passport.doman.service.impl;

import com.tarena.passport.common.pojo.dto.UserAddNewDTO;
import com.tarena.passport.common.pojo.dto.UserLoginDTO;
import com.tarena.passport.common.pojo.model.User;
import com.tarena.passport.doman.repository.UserRepository;
import com.tarena.passport.doman.service.IUserService;

import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ResultEnum;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void addNewUser(UserAddNewDTO userAddNewDTO) throws PassportBusinessException {
        User user = userRepository.getUserByUsername(userAddNewDTO.getUsername());
        if (user != null)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
        user = userRepository.getUserByPhone(userAddNewDTO.getPhone());
        if (user != null)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
        user = userRepository.getUserByMail(userAddNewDTO.getEmail());
        if (user != null)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
        user = new User();
        BeanUtils.copyProperties(userAddNewDTO, user);
        LocalDateTime now = LocalDateTime.now();
        user.setPassword(passwordEncoder.encode(userAddNewDTO.getPassword())).setGmtCreate(now).setGmtModified(now);
        int row = userRepository.addNewUser(user);
        if (row != 1)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) throws PassportBusinessException {
        String username = userLoginDTO.getUsername();
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            user = userRepository.getUserByPhone(username);
        }
        if (user == null) {
            user = userRepository.getUserByMail(username);
        }
        if (user == null) {
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new PassportBusinessException(ResultEnum.TOKEN_PASSWORD_ERROR);
        }
        return UUID.randomUUID().toString().replace("-", "");

    }
}
