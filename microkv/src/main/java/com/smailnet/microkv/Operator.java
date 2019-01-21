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
import android.preference.PreferenceManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Operator类是MicroKV组件的核心类，它主要负责对SharedPreferences生成的XML文件
 * 进行数据的增删改查，还有加密解密等判断处理。
 */
class Operator {

    private String filname;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean encryption;

    @SuppressLint("CommitPrefEdits")
    Operator(String filename, boolean encryption){
        Context context = Manager.getContext();
        this.encryption = encryption;
        if (filename == null){
            this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
            this.editor = preferences.edit();
        }else {
            this.filname = filename;
            this.preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            this.editor = preferences.edit();
        }
    }

    void setInt(String key, int value) {
        editor.putInt(key, value);
    }

    void setFloat(String key, float value){
        editor.putFloat(key, value);
    }

    void setLong(String key, long value){
        editor.putLong(key, value);
    }

    void setBoolean(String key, boolean value){
        editor.putBoolean(key, value);
    }

    void setString(String key, String value){
        value = (encryption) ? Security.encrypt(value, Manager.getKey()) : value;
        editor.putString(key, value);
    }

    void setStringSet(String key, Set<String> value){
        editor.putStringSet(key, value);
    }

    int getInt(String key, int value){
        return preferences.getInt(key, value);
    }

    float getFloat(String key, float value){
        return preferences.getFloat(key, value);
    }

    long getLong(String key, long value){
        return preferences.getLong(key, 0);
    }

    boolean getBoolean(String key, boolean value){
        return preferences.getBoolean(key, value);
    }

    String getString(String key, String value){
        String data = preferences.getString(key, value);
        return  (encryption && !data.equals(value)) ? Security.decrypt(data, Manager.getKey()) : data;
    }

    Set<String> getStringSet(String key, Set<String> value){
        return preferences.getStringSet(key, value);
    }

    Map<String, ?> getAll(){
        if (!encryption){
            return preferences.getAll();
        }else {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, ?> originalMap = preferences.getAll();
            for (Object object : originalMap.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                if (entry.getValue() instanceof String) {
                    String data = (String) entry.getValue();
                    data = Security.decrypt(data, Manager.getKey());
                    resultMap.put((String) entry.getKey(), data);
                }else {
                    resultMap.put((String) entry.getKey(), entry.getValue());
                }
            }
            return resultMap;
        }
    }

    SharedPreferences getSharedPreferences(){
        return preferences;
    }

    boolean containsKey(String key){
        Map<String, ?> map = preferences.getAll();
        return map.containsKey(key);
    }

    boolean comparisonKey(String key, Object comparisonValue){
        Map<String, ?> map = preferences.getAll();
        Object object = map.get(key);
        if (object == null){
            return false;
        }else if (object instanceof String && encryption){
            String data = String.valueOf(object);
            data = Security.decrypt(data, Manager.getKey());
            assert data != null;
            return data.equals(comparisonValue);
        }else {
            return object.equals(comparisonValue);
        }
    }

    void save(){
        editor.apply();
    }

    boolean commit(){
        return editor.commit();
    }

    void clear(){
        editor.clear().apply();
    }

    void removeValueForKey(String key){
        editor.remove(key).apply();
    }

    void removeValuesForKeys(String[] key){
        for (String s : key){
            editor.remove(s);
        }
        editor.apply();
    }

    boolean delete(){
        File file = new File(Manager.getFilePath(filname));
        return file.exists() && file.delete();
    }
}
