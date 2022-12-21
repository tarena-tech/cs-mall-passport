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

package com.tarena.passport.adaptor.controller.user;

import com.tarena.passport.adaptor.utils.AgentUtils;
import com.tarena.passport.adaptor.utils.IPUtils;
import com.tarena.passport.common.pojo.model.UserDO;
import com.tarena.passport.common.pojo.param.UserAddressAndBrowserNameParam;
import com.tarena.passport.common.pojo.param.UserLoginParam;
import com.tarena.passport.common.pojo.param.UserParam;
import com.tarena.passport.common.pojo.param.Check;
import com.tarena.passport.common.pojo.query.UserQuery;
import com.tarena.passport.doman.service.IUserService;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.result.JsonPage;
import com.tarena.passport.protocol.result.JsonResult;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/add-user")
    public JsonResult<String> addNewUser(@Validated(Check.Create.class) @RequestBody UserParam userParam,
        HttpServletRequest request) throws PassportBusinessException {
        userService.addNewUser(userParam, request);
        return JsonResult.ok("添加成功");
    }

    @PostMapping("/login")
    public JsonResult<String> login(@RequestBody UserLoginParam userLoginParam,
        HttpServletRequest request) throws PassportBusinessException {
        String address = IPUtils.getIpAddress(request);
        String browserName = AgentUtils.getLoginAgent(request);
        UserAddressAndBrowserNameParam param = new UserAddressAndBrowserNameParam(address, browserName);
        String token = userService.login(userLoginParam, param, request);
        return JsonResult.ok(token);
    }

    @GetMapping("/user-details")
    public JsonResult<UserView> userDetails(HttpServletRequest request) throws PassportBusinessException {
        UserDO userDO = userService.getUserDetails(request);
        UserView userView = new UserView();
        BeanUtils.copyProperties(userDO, userView);
        userView.setPassword("{protected}");
        return JsonResult.ok(userView);
    }

    @PostMapping("/user-list/{page}/{pageSize}")
    public JsonResult<JsonPage<UserView>> userList(@RequestBody UserQuery userQuery, HttpServletRequest request,
        @PathVariable Integer page, @PathVariable Integer pageSize) throws PassportBusinessException {
        JsonPage<UserDO> userDOList = userService.getUserList(userQuery, request, page, pageSize);
        JsonPage<UserView> jsonPage = new JsonPage<>();
        BeanUtils.copyProperties(userDOList, jsonPage);
        return JsonResult.ok(jsonPage);
    }

    @PostMapping("/deleteById/{id}")
    public JsonResult deleteUserById(@PathVariable Long id,
        HttpServletRequest request) throws PassportBusinessException {
        userService.deleteUserById(id, request);
        return JsonResult.ok("删除成功");
    }

    @PostMapping("/{id}")
    public JsonResult selectUserById(@PathVariable Long id,
        HttpServletRequest request) throws PassportBusinessException {
        UserDO userDO = userService.selectUserById(id, request);
        return JsonResult.ok(userDO);
    }

    @PostMapping("/update")
    public JsonResult updateUser(@RequestBody UserParam user,
        HttpServletRequest request) throws PassportBusinessException {
        userService.updateUser(user, request);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/enable")
    public JsonResult setEnable(@PathVariable Long id, HttpServletRequest request) throws PassportBusinessException {
        userService.setEnable(id, request);
        return JsonResult.ok("修改成功");
    }

    @PostMapping("/{id:[0-9]+}/disable")
    public JsonResult setDisable(@PathVariable Long id, HttpServletRequest request) throws PassportBusinessException {
        userService.setDisable(id, request);
        return JsonResult.ok("修改成功");
    }

}
