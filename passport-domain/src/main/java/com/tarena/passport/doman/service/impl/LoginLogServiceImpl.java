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

import com.tarena.passport.common.pojo.query.LoginLogQuery;
import com.tarena.passport.doman.repository.LoginLogRepository;
import com.tarena.passport.doman.service.ILoginLogService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginLogServiceImpl implements ILoginLogService {

    @Autowired private LoginLogRepository logServiceRepository;

    @Override public List<LoginLogQuery> getList(LoginLogQuery logQuery) {
        List<LoginLogQuery> list = logServiceRepository.getList(logQuery);
        return list;
    }

    @Override public void deleteById(Long id) {
        logServiceRepository.deleteById(id);
    }
}
