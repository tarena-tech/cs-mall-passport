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

import com.tarena.passport.common.pojo.model.UserDO;
import com.tarena.passport.common.pojo.model.UserLogDO;
import com.tarena.passport.common.pojo.param.UserAddressAndBrowserNameParam;
import com.tarena.passport.common.pojo.param.UserLoginParam;
import com.tarena.passport.common.pojo.param.UserParam;
import com.tarena.passport.doman.repository.UserRepository;
import com.tarena.passport.doman.service.IUserService;

import com.tarena.passport.doman.utils.PasswordEncoder;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ResultEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void addNewUser(UserParam userParam) throws PassportBusinessException {
        UserDO userDO = userRepository.getUserByUsername(userParam.getUsername());
        if (userDO != null)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
        userDO = userRepository.getUserByPhone(userParam.getPhone());
        if (userDO != null)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
        userDO = userRepository.getUserByMail(userParam.getEmail());
        if (userDO != null)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
        userDO = new UserDO();
        BeanUtils.copyProperties(userParam, userDO);
        LocalDateTime now = LocalDateTime.now();
        String password = passwordEncoder.encoder(userParam.getPassword());
        userDO.setPassword(password).setGmtCreate(now).setGmtModified(now);
        int row = userRepository.addNewUser(userDO);
        if (row != 1)
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
    }

    @Override

    public String login(UserLoginParam userLoginParam, UserAddressAndBrowserNameParam userAddressAndBrowserNameparam) throws PassportBusinessException {

        if (userAddressAndBrowserNameparam.getAddress()==null) throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        if (userAddressAndBrowserNameparam.getBrowserName()==null) throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        String username = userLoginParam.getUsername();
        UserDO userDO = userRepository.getUserByUsername(username);
        if (userDO == null) {
            userDO = userRepository.getUserByPhone(username);
        }
        if (userDO == null) {
            userDO = userRepository.getUserByMail(username);
        }
        if (userDO == null) {
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }
        if (!passwordEncoder.matches(userLoginParam.getPassword(),userDO.getPassword())) {
            throw new PassportBusinessException(ResultEnum.TOKEN_PASSWORD_ERROR);
        }
        log.info("登录用户信息{}",userDO);
        log.info("登录设备{}",userAddressAndBrowserNameparam);
        UserLogDO log = new UserLogDO();
        log.setAdminId(userDO.getId())
            .setIp(userAddressAndBrowserNameparam.getAddress())
            .setNickname(userDO.getNickname())
            .setUsername(userDO.getUsername())
            .setGmtLogin(LocalDateTime.now())
            .setUserAgent(userAddressAndBrowserNameparam.getBrowserName());
        int row = userRepository.insertUserLog(log);
        if (row!=1) throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        return UUID.randomUUID().toString().replace("-", "");

    }
}
