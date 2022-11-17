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

package com.tarena.passport.adaptor.controller;

import com.tarena.passport.adaptor.utils.AgentUtils;
import com.tarena.passport.adaptor.utils.IPUtils;
import com.tarena.passport.common.pojo.param.UserAddressAndBrowserNameParam;
import com.tarena.passport.common.pojo.param.UserLoginParam;
import com.tarena.passport.common.pojo.param.UserParam;
import com.tarena.passport.common.pojo.param.Check;
import com.tarena.passport.common.pojo.view.UserView;
import com.tarena.passport.doman.service.IUserService;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.result.JsonResult;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController{

    @Autowired
    private IUserService userService;

    @PostMapping("/add-user")
    public JsonResult<String> addNewUser(@Validated(Check.Create.class) @RequestBody UserParam userParam) throws PassportBusinessException {

        userService.addNewUser(userParam);
        return JsonResult.ok("注册成功");
    }

    @PostMapping("/login")
    public JsonResult<String> login(@RequestBody UserLoginParam userLoginParam, HttpServletRequest request) throws PassportBusinessException {
        String address = IPUtils.getIpAddress(request);
        String browserName = AgentUtils.getLoginAgent(request);
        UserAddressAndBrowserNameParam param = new UserAddressAndBrowserNameParam(address,browserName);
        String token=userService.login(userLoginParam,param);
        return JsonResult.ok(token);
    }

    @GetMapping("/user-details")
    public JsonResult<UserView> userDetails(HttpServletRequest request) throws PassportBusinessException {
        String jwt = request.getHeader("Authorization");
        UserView userDetails = userService.getUserDetails(jwt);
        return JsonResult.ok(userDetails);
    }


}
