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

import com.tarena.passport.common.pojo.dto.UserAddNewDTO;
import com.tarena.passport.common.pojo.dto.UserLoginDTO;
import com.tarena.passport.doman.service.IUserService;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.result.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController{

    @Autowired
    private IUserService userService;

    @PostMapping("/add-user")
    public JsonResult<String> addNewUser(@Validated UserAddNewDTO userAddNewDTO) throws PassportBusinessException {
        userService.addNewUser(userAddNewDTO);
        return JsonResult.ok("注册成功");
    }

    @PostMapping("/login")
    public JsonResult<String> login(UserLoginDTO userLoginDTO) throws PassportBusinessException {
        String token=userService.login(userLoginDTO);
        return JsonResult.ok(token);
    }


}
