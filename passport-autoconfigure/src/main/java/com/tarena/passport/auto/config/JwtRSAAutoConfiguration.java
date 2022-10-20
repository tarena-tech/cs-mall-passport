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
package com.tarena.passport.auto.config;

import com.tarena.passport.auto.domain.JwtRSAGenerator;
import com.tarena.passport.auto.properties.JwtRSAEncodeProperties;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "jwt.rsa", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(JwtRSAEncodeProperties.class)
public class JwtRSAAutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(JwtRSAAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(JwtRSAGenerator.class)
    public JwtRSAGenerator initJwtComponent(JwtRSAEncodeProperties properties) throws PassportBusinessException {
        JwtRSAGenerator generator = new JwtRSAGenerator();
        if (StringUtils.isEmpty(properties.getPrivateKey())) {
            logger.warn("your properties does not contain privateKey,this jwt generator cannot encode jwt");
        } else {
            String begin = "-----BEGIN PRIVATE KEY-----";
            String end = "-----END PRIVATE KEY-----";
            generator.setPrivateKey(properties.getPrivateKey().replace(begin, "").replace(end, "").replaceAll("\\s", ""));
        }
        if (StringUtils.isEmpty(properties.getPublicKey())) {
            logger.warn("your properties does not contain publicKey,this jwt generator cannot decode jwt");
        } else {
            String begin = "-----BEGIN PUBLIC KEY-----";
            String end = "-----END PUBLIC KEY-----";
            generator.setPublicKey(properties.getPublicKey().replace(begin, "").replace(end, "").replaceAll("\\s", ""));
        }
        if (properties.getExpiration() == null || properties.getExpiration() <= 0) {
            logger.error("your properties must have the value of expiration,but it is null");
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }
        generator.setExpiration(properties.getExpiration());
        return generator;
    }

}
