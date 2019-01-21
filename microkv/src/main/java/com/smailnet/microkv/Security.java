/**
 * Copyright (C) Lake Zhang, MicroKV Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smailnet.microkv;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Security类用于获取SecretKeySpec和对数据进行加密与与解密，其加密类型为AES加密
 * SecretKeySpec是动态生成，其不会存储在设备的存储空间中。其中SecretKeySpec会因
 * 开发者填写的Key不同而不同。
 */
class Security {

    //加密类型
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";

    //设置字符的编码
    private static final String CHARSET = "UTF-8";

    //设置iv
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    //AESCrypt-ObjC使用SHA-256（以及256位的密钥）
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * 获取SecretKeySpec
     * @return 返回SecretKeySpec对象
     */
    private static SecretKeySpec getSecretKey(String key){
        try {
            final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] bytes = key.getBytes(CHARSET);
            digest.update(bytes, 0, bytes.length);
            byte[] digestBytes = digest.digest();
            return new SecretKeySpec(digestBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文本加密
     * @param content
     * @param key
     * @return 返回值为已加密的字符串
     */
    static String encrypt(String content, String key){
        try {
            SecretKeySpec secretKeySpec = getSecretKey(key);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encryption = cipher.doFinal(content.getBytes(CHARSET));
            return Base64.encodeToString(encryption, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文本解密
     * @param content
     * @param key
     * @return  返回值为已解密的字符串
     */
    static String decrypt(String content, String key){
        try {
            SecretKeySpec secretKeySpec = getSecretKey(key);
            byte[] bytes = Base64.decode(content, Base64.NO_WRAP);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            return new String(cipher.doFinal(bytes), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
