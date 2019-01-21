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

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Manager类主要用于对MicroKV组件的全局变量进行管理，MicroKV组件初始化时应该
 * 获取到一个Context，获取Context很重要，最好MicroKV组件在Application中进行
 * 初始化。对于key变量参数，主要用于对数据进行加密解密，若初始化时没有填写，则
 * 默认key = "MicroKV_Key"
 */
class Manager extends Application {

    //用于保存已获取到的Context。
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    //key的默认值为"MicroKV_Key"
    private static String key = "MicroKV_Key";

    /**
     * 设置Context
     * @param context
     */
    public static void setContext(Context context) {
        Manager.context = context;
    }

    /**
     * 获取Context
     * @return 返回一个Context对象
     */
    public static Context getContext(){
        return context;
    }

    /**
     * 设置key
     * @param key
     */
    public static void setKey(String key) {
        Manager.key = key;
    }

    /**
     * 获取key
     * @return 返回一个String类型的key值
     */
    public static String getKey(){
        return key;
    }

    /**
     * 获取文件路径
     * @param filename
     * @return  返回该文件在存储空间中完整的文件路径
     */
    public static String getFilePath(String filename){
        if (filename == null){
            return Environment.getDataDirectory()
                    + "/data/"
                    + context.getPackageName()
                    + "/shared_prefs/"
                    + context.getPackageName()
                    + "_preferences.xml";
        }else {
            return Environment.getDataDirectory()
                    + "/data/"
                    + context.getPackageName()
                    + "/shared_prefs/"
                    + filename
                    + ".xml";
        }
    }

}
