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

package com.tarena.passport.protocol.result;

import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ResultEnum;
import java.io.Serializable;
import lombok.Data;

@Data
public class JsonResult<T> implements Serializable {

    /**
     * 业务状态码
     */
    private Integer state;
    /**
     * 错误时的提示消息
     */
    private String message;
    /**
     * 成功时响应的数据
     */
    private T data;

    public JsonResult() {
    }

    private JsonResult(Integer state, String message, T data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    public static JsonResult<Void> ok() {
        return ok(null);
    }

    public static <T> JsonResult<T> ok(T data) {
        return new JsonResult(ResultEnum.OK.getCode(), null, data);
    }

    public static JsonResult<Void> fail(PassportBusinessException e) {
        return fail(e.getCode(), e.getMessage());
    }

    public static JsonResult<Void> fail(Integer state, String message) {
        return new JsonResult(state, message, null);
    }



}
