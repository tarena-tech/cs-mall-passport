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
package com.tarena.passport.auto.domain;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.tarena.passport.protocol.PassportBusinessException;
import com.tarena.passport.protocol.enums.ResultEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

@Slf4j
//JWT 令牌生成器
public class JwtRSAGenerator<T> {
    //私钥
    private String privateKey;
    //公钥
    private String publicKey;
    //过期时间
    private Long expiration;
    //登录的用户角色
    private static final String CLAIM_PAYLOAD = "PAYLOAD";
    //自定义创建时间
    private static final String CLAIM_CREATED = "CREATED";

    public String generateToken(T payload) throws PassportBusinessException {
        //准备jwt荷载对象claims,本质就是map,我们可以在里面自定义任意key和value
        Map<String, Object> claims = new HashMap<>();
        //存放日期时间
        claims.put(CLAIM_CREATED, new Date());
        //存放user对象
        claims.put(CLAIM_PAYLOAD, JSON.toJSONString(payload));
        return generateToken(claims, privateKey, expiration);
    }

    private String generateToken(Map<String, Object> claims, String privateKey,
        Long expiration) throws PassportBusinessException {
        //构建JWT
        JwtBuilder builder = null;
        if (privateKey == null) {
            //如果私钥为null，抛出异常
            log.error("you are using null privateKey to encode jwt");
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        } else {
            try {
                //如果 私钥不为null，则获取私钥
                RSAPrivateKey key = getRSAPrivateKey(privateKey);
                //构建JWT
                builder = Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate(claims, expiration)).signWith(SignatureAlgorithm.RS256, key);
            } catch (NoSuchAlgorithmException e) {
                log.error("failed to get rsa privateKey,please ensure your privateKey is correct");
                throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
            } catch (Exception e) {
                log.error("faild to generate rsa encoded jwt");
                throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
            }
        }
        return builder.compact();
    }
    //获取用户登录有效期
    private Date generateExpirationDate(Map<String, Object> claims, Long expiration) {
        //用户登录时间+有效时长 = 实际有限期
        return new Date(((Date) claims.get(CLAIM_CREATED)).getTime() + expiration);
    }

    //获取私钥
    private static RSAPrivateKey getRSAPrivateKey(String priKey) throws NoSuchAlgorithmException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKey));
        RSAPrivateKey privateKey = null;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        try {
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("get RSAPrivateKey fail ,priKey:[{}], err:[{}]", priKey, Throwables.getStackTraceAsString(e));
        }
        return privateKey;
    }

    //获取公钥
    private RSAPublicKey getRSAPublicKey(String pubKey) throws NoSuchAlgorithmException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
        RSAPublicKey publicKey = null;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        try {
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("get RSAPublicKey fail ,pubKey:[{}] ,err:[{}]", pubKey, Throwables.getStackTraceAsString(e));
        }
        return publicKey;
    }
    //解析JWT
    public T getLoginFromToken(String token, Class<? extends T> clazz) throws PassportBusinessException {
        Jws<Claims> jws = null;
        try {
            jws = Jwts.parser().setSigningKey(getRSAPublicKey(getPublicKey())).parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            log.error("payload is expired");
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        } catch (SignatureException ex) {
            log.error("signature is not matched, this jwt may be edited by an invator");
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        } catch (IllegalArgumentException ex) {
            log.error("token is empty");
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        } catch (Exception e) {
            log.error("parse token fail, token:[{}], publicKey:[{}], err:[{}]", token, publicKey, Throwables.getStackTraceAsString(e));
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }
        if (jws == null) {
            log.error("jws parsed result is null");
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }
        Claims claims = jws.getBody();
        try {
            String loginTokenJson = (String) claims.get(CLAIM_PAYLOAD);
            return JSON.parseObject(loginTokenJson,clazz);
        } catch (ClassCastException e) {
            log.error("token is not matched the parse pattern");
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        } catch (Exception e) {
            log.error("clazz:{} cannot use as the parse object Class", clazz.getName());
            throw new PassportBusinessException(ResultEnum.SYSTEM_ERROR);
        }
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
