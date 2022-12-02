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

package com.tarena.passport.protocol.enums;

import lombok.Getter;

@Getter
public enum LogEnum {


    USER_PASSPORD_ERROR("密码错误"),
    USER_NOTFOUND_ERROR("用户不存在"),
    SYS_USER_DISABLE("用户已被禁用"),

    SYSTEM_ERROR( "系统错误");

    LogEnum(String message) {
        this.message = message;
    }


    private String message;
}
