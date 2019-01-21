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
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * MicroKV是一个基于安卓SharedPreferences封装的键值对组件。SharedPreferences是
 * Android平台上一个轻量级的存储类，日常在项目都会使用到它对数据进行持久化，但该
 * 存储类并没有提供数据加密功能，所以MicroKV基于此封装成一个简单易用的键值对组件，
 * 并提供AES加密数据的功能，同时支持SharedPreferences文件的删除，查询键值是否存
 * 在，键值对的比较等功能。
 *
 * @author 张观湖
 * @author E-mail: zguanhu@foxmail.com
 * @version 1.2.0
 */
public class MicroKV {

    private Operator operator;

    @SuppressLint("CommitPrefEdits")
    private MicroKV(Operator operator){
        this.operator = operator;
    }

    /**
     * MicroKV组件初始化，获取context，传入给Manager类的setContext()方法
     * @param context
     */
    public static void initialize(Context context){
        Manager.setContext(context);
    }

    /**
     * MicroKV组件初始化，获取context和key，传入给Manager类的setContext()和setKey()方法。
     * @param context
     * @param key
     */
    public static void initialize(Context context, String key){
        Manager.setContext(context);
        Manager.setKey(key);
    }

    /**
     * defaultMicroKV()方法获得的MicroKV对象，使用该对象所进行文件读写操作时，
     * SharedPreference文件名称是使用当前应用程序的包名作为前缀名来命名。
     * @return 返回MicroKV对象实例
     */
    public static MicroKV defaultMicroKV(){
        Operator operator = new Operator(null, false);
        return new MicroKV(operator);
    }

    /**
     * customize()可以自定义SharedPreference文件的文件名称。
     * 参数一：SharedPreference文件名称。
     * 参数二：是否开启加密，默认为不开启。
     * @param filename
     * @return 返回MicroKV对象实例
     */
    public static MicroKV customize(String filename){
        Operator operator = new Operator(filename, false);
        return new MicroKV(operator);
    }

    /**
     * customize()可以自定义SharedPreference文件的文件名称。
     * 参数一：SharedPreference文件名称。
     * 参数二：是否开启加密，默认为不开启。
     * @param filename
     * @param encryption
     * @return 返回MicroKV对象实例
     */
    public static MicroKV customize(String filename, boolean encryption){
        Operator operator = new Operator(filename, true);
        return new MicroKV(operator);
    }

    public MicroKV setKV(String key, int value){
        operator.setInt(key, value);
        return this;
    }

    public MicroKV setKV(String key, float value){
        operator.setFloat(key, value);
        return this;
    }

    public MicroKV setKV(String key, long value){
        operator.setLong(key, value);
        return this;
    }

    public MicroKV setKV(String key, boolean value){
        operator.setBoolean(key, value);
        return this;
    }

    public MicroKV setKV(String key, String value){
        operator.setString(key, value);
        return this;
    }

    public MicroKV setKV(String key, Set<String> value){
        operator.setStringSet(key, value);
        return this;
    }

    public int getKV(String key, int defaultValue){
        return operator.getInt(key, defaultValue);
    }

    public float getKV(String key, float defaultValue){
        return operator.getFloat(key, defaultValue);
    }

    public long getKV(String key, long defaultValue){
        return operator.getLong(key, defaultValue);
    }

    public boolean getKV(String key, boolean defaultValue){
        return operator.getBoolean(key, defaultValue);
    }

    public String getKV(String key, String defaultValue){
        return operator.getString(key, defaultValue);
    }

    public Set<String> getKV(String key, Set<String> defaultValue){
        return operator.getStringSet(key, defaultValue);
    }

    public int getInt(String key){
        return operator.getInt(key, 0);
    }

    public float getFloat(String key){
        return operator.getFloat(key, 0);
    }

    public long getLong(String key){
        return operator.getLong(key, 0);
    }

    public boolean getBoolean(String key){
        return operator.getBoolean(key, false);
    }

    public String getString(String key){
        return operator.getString(key, "");
    }

    public Set<String> getStringSet(String key){
        return operator.getStringSet(key, null);
    }

    /**
     * 获取该SharedPreference文件中全部键值对的值
     * @return 返回值为Map对象
     */
    public Map<String, ?> getAll(){
        return operator.getAll();
    }

    /**
     * 获取SharedPreferences对象实例
     * @return 返回值为SharedPreferences对象
     */
    public SharedPreferences getSharedPreferences(){
        return operator.getSharedPreferences();
    }

    /**
     * containsKV()方法用于判断SharedPreference文件中该key是否存在。
     * @param key
     * @return 存在返回值为true，不存在返回值为false
     */
    public boolean containsKV(String key){
        return operator.containsKey(key);
    }

    /**
     * comparisonKV()方法用于比较一个值与存储在内存空间中某个键值是否相同
     * @param key
     * @param comparisonValue
     * @return 相同返回值为true，不相同返回值为false
     */
    public boolean comparisonKV(String key, Object comparisonValue){
        return operator.comparisonKey(key, comparisonValue);
    }

    /**
     * 异步保存SharedPreference中的内容，相当于原生的apply()方法。
     */
    public void save(){
        operator.save();
    }

    /**
     * 同步保存SharedPreference中的内容，相当于原生的commit()方法。
     * @return 保存成功返回值为true，保存失败返回值为false
     */
    public boolean commit(){
        return operator.commit();
    }

    /**
     * 清除SharedPreference文件中所以键值对的内容。
     */
    public void clear(){
        operator.clear();
    }

    /**
     * 移除SharedPreference文件中某一个键值对。
     * @param key
     */
    public void removeKV(String key){
        operator.removeValueForKey(key);
    }

    /**
     * 移除SharedPreference文件中某一组键值对。
     * @param key
     */
    public void removeKVs(String[] key){
        operator.removeValuesForKeys(key);
    }

    /**
     * 删除该SharedPreference文件。
     * @return 删除成功返回值为true，删除失败返回值为false
     */
    public boolean delete(){
        return operator.delete();
    }
}
