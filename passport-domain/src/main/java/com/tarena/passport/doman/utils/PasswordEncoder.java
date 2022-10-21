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

package com.tarena.passport.doman.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordEncoder {
    private static final String[] version = {"$2a", "$2y", "$2b"};

    public PasswordEncoder() {

    }

    private String getsal() {
        Random random = new Random();
        int num = random.nextInt(3);
        String replace = version[num] + UUID.randomUUID().toString().replace("-", "");
        char[] chars = replace.toCharArray();

        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(chars.length - 3) + 3;
            chars[index] = '.';
        }
        replace = new String(chars);
        return replace;
    }

    public String encoder(String password) {
        String getsal = getsal();
        String encoder = md5Encoder(password);
        return passEncoder(getsal, encoder);
    }

    private static String passEncoder(String getsal, String encoder) {
        return checkingRule(getsal, encoder);
    }

    private static String checkingRule(String getsal, String encoder) {
        char[] getsalc = getsal.toCharArray();
        char[] encoderc = encoder.toCharArray();
        int getsalcIndex = 0;
        int encodercIndex = 0;
        char[] chars = new char[getsalc.length + encoderc.length];
        for (int i = 0; i < chars.length; i++) {
            if (getsalcIndex != getsalc.length) {
                if (i % 2 == 0)
                    chars[i] = getsalc[getsalcIndex++];
            }
            if (encodercIndex != encoderc.length) {
                if (i % 2 != 0)
                    chars[i] = encoderc[encodercIndex++];
            }
        }

        return new String(chars).trim().substring(0,62);
    }

   private static String md5Encoder(String password) {
        String md5 = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] result = digest.digest(password.getBytes());
            md5 = toHex(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    private static String toHex(byte[] result) {
        if (null == result) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            int high = result[i] >> 4 & 0x0f;
            int low = result[i] & 0x0f;
            sb.append(Character.forDigit(high, 16)).append(Character.forDigit(low, 16));
        }
        return sb.toString();
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.length() == 0) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        String passEncoder = encoder(rawPassword);
        return checkpw(passEncoder, encodedPassword);
    }

    private boolean checkpw(String encoder, String password) {
        final int[] rules = {
            0, 1, 2, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23,
            25, 27, 29, 30, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 57, 59, 61};
        char[] encoderChars = encoder.toCharArray();
        char[] passwordChars = password.toCharArray();
        StringBuilder encoderBuilder = new StringBuilder();
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < rules.length; i++) {
            encoderBuilder.append(encoderChars[rules[i]]);

        }
        for (int i = 0; i < rules.length; i++) {
            passwordBuilder.append(passwordChars[rules[i]]);
        }


        return encoderBuilder.toString().equals(passwordBuilder.toString());
    }
}
