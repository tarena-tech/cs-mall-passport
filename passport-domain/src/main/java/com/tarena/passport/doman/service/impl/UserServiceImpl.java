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

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tarena.passport.auto.domain.JwtRSAGenerator;
import com.tarena.passport.common.pojo.model.LogDetailDO;
import com.tarena.passport.common.pojo.model.UserDO;
import com.tarena.passport.common.pojo.model.UserLogDO;
import com.tarena.passport.common.pojo.param.UserAddressAndBrowserNameParam;
import com.tarena.passport.common.pojo.param.UserLoginParam;
import com.tarena.passport.common.pojo.param.UserOperateParam;
import com.tarena.passport.common.pojo.param.UserParam;
import com.tarena.passport.common.pojo.query.UserQuery;
import com.tarena.passport.doman.repository.OperateLogRepository;
import com.tarena.passport.doman.repository.UserRepository;
import com.tarena.passport.doman.service.IUserService;
import com.tarena.passport.doman.utils.LogDetail;
import com.tarena.passport.doman.utils.OperateDetail;
import com.tarena.passport.doman.utils.PasswordEncoder;
import com.tarena.passport.protocol.LoginInfo;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.LogEnum;
import com.tarena.passport.protocol.enums.ResultEnum;
import com.tarena.passport.protocol.result.JsonPage;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRSAGenerator<LoginInfo> jwtRSAGenerator;

    @Autowired
    private OperateLogRepository operateLogRepository;

    @Override
    public void addNewUser(UserParam userParam, HttpServletRequest request) throws PassportBusinessException {
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
        Date date = new Date();
        String password = passwordEncoder.encoder(userParam.getPassword());
        userDO.setPassword(password).setGmtCreate(date).setGmtModified(date);
        int row = userRepository.addNewUser(userDO);

        UserDO user = getUserDetails(request);
        UserOperateParam param = new UserOperateParam();
        userParam.setPassword("{protected}");
        param.setAdminId(user.getId())
            .setRequestParameter(JSON.toJSON(userParam).toString())
            .setGmtOperate(LocalDateTime.now())
            .setState(1)
            .setDetail(LogDetail.getLogDetail(request))
            .setOperateMethod(Thread.currentThread().getStackTrace()[1].getMethodName());

        if (row != 1) {
            param.setState(0).setDetail(LogEnum.SYSTEM_ERROR.getMessage());
            operateLogRepository.insertOperateLog(param);
            throw new PassportBusinessException(ResultEnum.SYS_USER_ALREADY_EXISTS);
        }
        log.info("param:",param);
        operateLogRepository.insertOperateLog(param);
    }

    @Override

    public String login(UserLoginParam userLoginParam, UserAddressAndBrowserNameParam userAddressAndBrowserNameparam,
        HttpServletRequest request) throws PassportBusinessException {

        if (userAddressAndBrowserNameparam.getAddress() == null)
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        if (userAddressAndBrowserNameparam.getBrowserName() == null)
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
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
        int state = 1;
        log.info("登录用户信息{}", userDO);
        log.info("登录设备{}", userAddressAndBrowserNameparam);
        UserLogDO log = new UserLogDO();
        log.setAdminId(userDO.getId())
            .setIp(userAddressAndBrowserNameparam.getAddress())
            .setNickname(userDO.getNickname())
            .setUsername(userDO.getUsername())
            .setGmtLogin(new Date())
            .setUserAgent(userAddressAndBrowserNameparam.getBrowserName());
        int row = userRepository.insertUserLog(log);
        String detail = LogDetail.getLogDetail(request);
        LogDetailDO logDetail = new LogDetailDO();
        logDetail.setLoginId(log.getId());
        logDetail.setDetail(detail);
        if (row != 1) {
            state = 0;
            insertLogDetail(state, LogEnum.SYSTEM_ERROR.getMessage(), logDetail);
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }

        if (!passwordEncoder.matches(userLoginParam.getPassword(), userDO.getPassword())) {
            state = 0;
            logDetail.setState(state);
            logDetail.setDetail(LogEnum.USER_PASSPORD_ERROR.getMessage());
            userRepository.insertUserLogDetail(logDetail);
            throw new PassportBusinessException(ResultEnum.TOKEN_PASSWORD_ERROR);

        }
        if (userDO.getEnable() != 1) {
            state = 0;
            insertLogDetail(state, LogEnum.SYS_USER_DISABLE.getMessage(), logDetail);
            throw new PassportBusinessException(ResultEnum.SYS_USER_DISABLE);
        }
        logDetail.setState(state);
        userRepository.insertUserLogDetail(logDetail);
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(userDO.getId());
        String jwt = jwtRSAGenerator.generateToken(loginInfo);
        return jwt;

    }

    @Override
    public UserDO getUserDetails(HttpServletRequest request) throws PassportBusinessException {

        LoginInfo loginInfo = jwtRSAGenerator.getLoginFromToken(request.getHeader("Authorization"), LoginInfo.class);
        if (loginInfo == null) {
            throw new PassportBusinessException(ResultEnum.TOKEN_EXPIRES);
        }
        UserDO userDO = userRepository.getUserByUserID(loginInfo.getId());

        if (userDO == null) {
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }

        return userDO;
    }

    @Override public JsonPage<UserDO> getUserList(UserQuery query, HttpServletRequest request, Integer page,
        Integer pageSize) throws PassportBusinessException {
        PageHelper.startPage(page, pageSize);
        List<UserDO> userList = userRepository.getUserList(query);
        UserDO userDO = getUserDetails(request);
        UserOperateParam userOperateParam = OperateDetail.KeepOperateDetail(userDO, query, request, Thread.currentThread().getStackTrace()[1].getMethodName());
        operateLogRepository.insertOperateLog(userOperateParam);

        return JsonPage.restPage(new PageInfo<>(userList));
    }

    @Override public void deleteUserById(Long id, HttpServletRequest request) throws PassportBusinessException {
        UserDO userByUserID = userRepository.getUserByUserID(id);
        UserDO userDO = getUserDetails(request);
        UserOperateParam userOperateParam = OperateDetail.KeepOperateDetail(
            userDO, id,
            request,
            Thread.currentThread().getStackTrace()[1].getMethodName());
        if (userByUserID == null) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.USER_NOTFOUND_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }

        operateLogRepository.insertOperateLog(userOperateParam);
        userRepository.deleteUserById(id);

    }

    @Override public UserDO selectUserById(Long id, HttpServletRequest request) {
        UserDO userDO = userRepository.selectUserById(id);
        return userDO;
    }

    @Override public void updateUser(UserParam user, HttpServletRequest request) throws PassportBusinessException {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(user, userDO);
        userDO.setGmtModified(new Date());
        int row = userRepository.updateUser(userDO);
        UserDO user2 = getUserDetails(request);
        UserOperateParam userOperateParam = OperateDetail.KeepOperateDetail(
            user2, user,
            request,
            Thread.currentThread().getStackTrace()[1].getMethodName());
        if (row == 0) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.USER_NOTFOUND_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }
        operateLogRepository.insertOperateLog(userOperateParam);

    }

    @Override
    public void setEnable(Long id, HttpServletRequest request) throws PassportBusinessException {
        UserDO user = getUserDetails(request);
        UserOperateParam userOperateParam = OperateDetail.KeepOperateDetail(
            user, id,
            request,
            Thread.currentThread().getStackTrace()[1].getMethodName());
        if (id == 1) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.USER_NOTFOUND_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }
        UserDO userDO = userRepository.selectUserById(id);
        if (userDO == null) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.USER_NOTFOUND_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }

        if (userDO.getEnable() == 1) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.SYSTEM_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.OK);
        }

        userDO.setEnable(1);
        int rows = userRepository.updateUser(userDO);
        if (rows != 1) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.SYSTEM_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }
        operateLogRepository.insertOperateLog(userOperateParam);
    }

    @Override
    public void setDisable(Long id, HttpServletRequest request) throws PassportBusinessException {
        log.debug("开始处理【禁用管理员账号】的业务：id={}", id);
        UserDO user = getUserDetails(request);
        UserOperateParam userOperateParam = OperateDetail.KeepOperateDetail(
            user, id,
            request,
            Thread.currentThread().getStackTrace()[1].getMethodName());
        if (id == 1) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.USER_NOTFOUND_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }

        UserDO userDO = userRepository.selectUserById(id);

        if (userDO == null) {
            throw new PassportBusinessException(ResultEnum.SYS_USER_NON_EXISTENT);
        }

        if (userDO.getEnable() == 0) {

            throw new PassportBusinessException(ResultEnum.OK);
        }

        userDO.setEnable(0);
        int rows = userRepository.updateUser(userDO);

        if (rows != 1) {
            userOperateParam.setState(0);
            userOperateParam.setDetail(LogEnum.USER_NOTFOUND_ERROR.getMessage());
            operateLogRepository.insertOperateLog(userOperateParam);
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }
        operateLogRepository.insertOperateLog(userOperateParam);
    }

    public int insertLogDetail(int state, String detail, LogDetailDO logDetail) {
        logDetail.setState(state);
        logDetail.setDetail(detail);
        return userRepository.insertUserLogDetail(logDetail);
    }
}
