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

package com.tarena.passport.adaptor.controller.log;

import com.tarena.passport.common.pojo.query.LoginLogQuery;
import com.tarena.passport.doman.service.ILoginLogService;
import com.tarena.passport.protocol.result.JsonResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/log/login")
@RestController
public class LoginLogControler {

    @Autowired
    private ILoginLogService loginLogService;

    @PostMapping("/list")
    public JsonResult<List<LoginView>> getAll(@RequestBody LoginLogQuery logQuery){
        System.out.println("logQuery = " + logQuery);
        List<LoginLogQuery> list = loginLogService.getList(logQuery);
        ArrayList<LoginView> views = new ArrayList<>();
        for (LoginLogQuery query : list) {
            LoginView loginView = new LoginView();
            BeanUtils.copyProperties(query,loginView);
            views.add(loginView);
        }
        return JsonResult.ok(views);

    }

    @PostMapping("/delete/{id}")
    public JsonResult getAll(@PathVariable Long id) {
        loginLogService.deleteById(id);
        return JsonResult.ok("删除成功");
    }
}
