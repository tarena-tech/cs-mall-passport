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

package com.tarena.passport.adaptor.utils;

import javax.servlet.http.HttpServletRequest;

public class AgentUtils {
    public static String getLoginAgent(HttpServletRequest request) {
        String browserName = "";
        String userAgent = request.getHeader("User-Agent").toUpperCase();
        if (userAgent == null || userAgent.equals("")) {
            return "";
        }
        if (userAgent.indexOf("MSIE") > 0) {
            browserName = "IE";
        } else if (userAgent.indexOf("FIREFOX") > 0) {
            browserName = "Firefox";
        } else if (userAgent.indexOf("CHROME") > 0) {
            browserName = "Chrome";
        } else if (userAgent.indexOf("SAFARI") > 0) {
            browserName = "Safari";
        } else if (userAgent.indexOf("CAMINO") > 0) {
            browserName = "Camino";
        } else if (userAgent.indexOf("KONQUEROR") > 0) {
            browserName = "Konqueror";
        } else if (userAgent.indexOf("EDGE") > 0) {
            browserName = "Microsoft Edge";
        }
        return browserName;

    }
}
