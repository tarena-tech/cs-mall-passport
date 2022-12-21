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

package com.tarena.passport.common.pojo.param;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserParam  implements Serializable {
    @NotNull(message = "id禁止为空",groups = Check.Update.class)
    private Long id;
    @NotNull(message = "请输入用户名！",groups = Check.Create.class)
    private String username;
    @NotNull(message = "请输入密码！",groups = Check.Create.class)
    private String password;
    @NotNull(message = "昵称！",groups = Check.Create.class)
    private String nickname;
    @NotNull(message = "请输入手机号！",groups = Check.Create.class)
    private String phone;
    @NotNull(message = "邮箱！",groups = Check.Create.class)
    private String email;
    private Integer enable;


}
