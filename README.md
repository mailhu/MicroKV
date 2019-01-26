# 一个简单易用的Android键值对组件

## 文档目录
* [MicroKV 简介](https://github.com/mailhu/microkv#microkv-%E7%AE%80%E4%BB%8B)
* [安装引入](https://github.com/mailhu/microkv#%E5%AE%89%E8%A3%85%E5%BC%95%E5%85%A5)
* [快速入门](https://github.com/mailhu/microkv#%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8)
  + [初始化](https://github.com/mailhu/microkv#-%E5%88%9D%E5%A7%8B%E5%8C%96)
  + [写入数据](https://github.com/mailhu/microkv#-%E5%86%99%E5%85%A5%E6%95%B0%E6%8D%AE)
  + [读取数据](https://github.com/mailhu/microkv#-%E8%AF%BB%E5%8F%96%E6%95%B0%E6%8D%AE)
* [详细教程](https://github.com/mailhu/microkv#%E8%AF%A6%E7%BB%86%E6%95%99%E7%A8%8B)
  + [初始化](https://github.com/mailhu/microkv#-%E5%88%9D%E5%A7%8B%E5%8C%96-1)
  + [写入数据与加密](https://github.com/mailhu/microkv#-%E5%86%99%E5%85%A5%E6%95%B0%E6%8D%AE%E4%B8%8E%E5%8A%A0%E5%AF%86)
  + [读取数据与解密](https://github.com/mailhu/microkv#-%E8%AF%BB%E5%8F%96%E6%95%B0%E6%8D%AE%E4%B8%8E%E8%A7%A3%E5%AF%86)
  + [读取全部数据](https://github.com/mailhu/microkv#-%E8%AF%BB%E5%8F%96%E5%85%A8%E9%83%A8%E6%95%B0%E6%8D%AE)
  + [更新数据](https://github.com/mailhu/microkv#-%E6%9B%B4%E6%96%B0%E6%95%B0%E6%8D%AE)
  + [查询数据](https://github.com/mailhu/microkv#-%E6%9F%A5%E8%AF%A2%E6%95%B0%E6%8D%AE)
  + [校对数据](https://github.com/mailhu/microkv#-%E6%A0%A1%E5%AF%B9%E6%95%B0%E6%8D%AE)
  + [移除数据](https://github.com/mailhu/microkv#-%E7%A7%BB%E9%99%A4%E6%95%B0%E6%8D%AE)
  + [清除数据](https://github.com/mailhu/microkv#-%E6%B8%85%E9%99%A4%E6%95%B0%E6%8D%AE)
  + [删除文件](https://github.com/mailhu/microkv#-%E5%88%A0%E9%99%A4%E6%96%87%E4%BB%B6)
  + [原生方法](https://github.com/mailhu/microkv#-%E5%8E%9F%E7%94%9F%E6%96%B9%E6%B3%95)
* [混淆](https://github.com/mailhu/microkv#%E6%B7%B7%E6%B7%86)
* [感谢](https://github.com/mailhu/microkv#%E6%84%9F%E8%B0%A2)
* [License](https://github.com/mailhu/microkv#license)

## MicroKV 简介
[![](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/mailhu/microkv/blob/master/LICENSE)
[![](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.google.cn/)
[![](https://jitpack.io/v/mailhu/microkv.svg)](https://jitpack.io/#mailhu/microkv)

&emsp;&emsp;MicroKV是一个基于安卓SharedPreferences封装的键值对组件。SharedPreferences是Android平台上一个轻量级的存储类，日常在项目都会使用到它对数据进行持久化，但该存储类并没有提供数据加密功能，所以MicroKV基于此封装成一个简单易用的键值对组件，并提供AES加密数据的功能，同时支持SharedPreferences文件的删除，查询键值是否存在，键值对的比较等功能。但MicroKV组件只适用于存储轻量级的数据，当需要存储大量数据和处理复杂的数据关系时，还是提倡使用SQLite进行数据缓存。

## 安装引入
步骤一、将JitPack存储库添加到根目录的build.gradle中：
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
步骤二、在项目的app模块下的build.gradle里加：
```gradle
dependencies {
    implementation 'com.github.mailhu:microkv:1.3.8'
}
```

## 快速入门
#### ● 初始化
在你的项目自定义的Application类中或者Activity中初始化MicroKV组件。例如在Application类里初始化，代码如下：
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //MicroKV组件默认的初始化方式
        MicroKV.initialize(this);
    }
}
```
#### ● 写入数据
向存储空间中写入数据，代码如下：
```java
//defaultMicroKV()方法获取MicroKV对象，同时使用当前应用程序的包名来命名文件名
MicroKV kv = MicroKV.defaultMicroKV();
kv.setKV("int", 1949);                              //写入一个int类型的数据
kv.setKV("long", 299792458L);                       //写入一个long类型的数据
kv.setKV("float", 3.14f);                           //写入一个float类型的数据
kv.setKV("bool", true);                             //写入一个boolean类型的数据
kv.setKV("string", "Hello MicroKV");                //写入一个String的数据
kv.save();                                          //保存数据
```
#### ● 读取数据
从存储空间中读取数据，代码如下：
```java
MicroKV kv = MicroKV.defaultMicroKV();

int intValue = kv.getInt("int");                    //获取int类型的数据，默认值0
long longValue = kv.getLong("long");                //获取long类型的数据，默认值0
float floatValue = kv.getFloat("float");            //获取float类型的数据，默认值0
boolean boolValue = kv.getBoolean("bool");          //获取boolean类型的数据，默认值false
String string = kv.getString("string");             //获取String类型的数据，默认值为空字符串
```

## 详细教程

#### ● 初始化
- 在你的项目自定义的Application类中或者Activity中初始化MicroKV组件。
- 使用 public static void initialize(Context context, String key) 方法
- 参数一：Context
- 参数二：字符串key由开发者自定义，主要是在对数据加密解密时使用，若不填写第二个参数，则key的默认值为Micro_Key。

例如在Application类里初始化，代码如下：
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //MicroKV组件带参数key的初始化
        MicroKV.initialize(this, "2018Year12Mon21Day");
    }
}
```
#### ● 写入数据与加密
**温馨提示：保存大量数据时，为了节省运行内存空间和执行时间，建议批量插入数据再调用save( )方法保存数据。**

使用默认的SharedPreferences文件名，写入数据，代码如下：
```java
MicroKV.defaultMicroKV()
        .setKV("int", 1)
        .setKV("float", 0.5f)
        .setKV("long", 1000L)
        .setKV("bool", true)
        .setKV("string", "Hello MicroKV")
        .save();
```
自定义SharedPreferences文件名，写入数据，代码如下：
```java
//customize( )方法的参数一：文件名；参数二：是否开启加密，若不填写，默认不开启。
MicroKV.customize("MyInfo")
        .setKV("nickname", "小学生")
        .setKV("age", 20)
        .setKV("address", "中国广东省广州市")
        .setKV("signature", "好好学习，天天向上")
        .save();
```
自定义SharedPreferences文件名并开启加密功能，写入数据，代码如下：
```java
//customize( )方法的参数一：文件名；参数二：是否开启加密，若不填写，默认不开启。
MicroKV.customize("Account", true)
        .setKV("username", "Lake")
        .setKV("password", "20190120")
        .setKV("updateAt", 1545383030448L)
        .save();
```
加密的内容如下：
```xml
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="username">9Cx8OGzwmj0uNcX6Ps45DQ==</string>
    <string name="password">BWhcIen8Y3gc3+tPW/1NgQ==</string>
    <long name="updateAt" value="1545383030448" />
</map>
```
#### ● 读取数据与解密
第一种读取方式，若读取的键值对不存在，该方法会自动返回一个默认值，代码如下：
```java
MicroKV kv = MicroKV.defaultMicroKV();
int i = kv.getInt("int");
long l = kv.getLong("long");
float f = kv.getFloat("float");
boolean b = kv.getBoolean("bool");
String s = kv.getString("string");
``` 
第二种读取方式，若读取的键值对不存在，开发者可以自定义默认的返回值，代码如下：
```java
MicroKV kv = MicroKV.customize("MyInfo");
String nickname = kv.getKV("username", "");
String address = kv.getKV("address", "中国");
String signature = kv.getKV("signature", "这个人很懒，什么也没写");
int age = kv.getKV("age", 0);
        
//float类型和long类型默认值中的数值末尾要添加相应的 “f” 和 “L”
float floatValue = kv.getKV("float", 0f);
long longValue = kv.getKV("long", 0L);
```
读取已加密的数据，customize( )方法的参数二必须为true，代码如下：
```java
MicroKV kv = MicroKV.customize("Account", true);
String username = kv.getString("username");
String password = kv.getString("password");
long updateAt = kv.getKV("updateAt", 0L);
```
#### ● 读取全部数据
读取默认文件名称的文件中的全部数据，代码如下：
```java
Map<String, ?> map = MicroKV.defaultMicroKV().getAll();

for (Map.Entry entry : map.entrySet()){
    Log.d(TAG, "Key = " + entry.getKey() + "    value = " + entry.getValue());
}
```
读取自定义文件名称的文件中的全部数据，代码如下：
```java
Map<String, ?> map = MicroKV.customize("MyInfo").getAll();

for (Map.Entry entry : map.entrySet()){
    Log.d(TAG, "Key = " + entry.getKey() + "    value = " + entry.getValue());
}
```
读取自定义文件名称的文件中的全部数据，且该文件已加密，代码如下：
```java
Map<String, ?> map = MicroKV.customize("Account", true).getAll();

for (Map.Entry entry : map.entrySet()){
    Log.d(TAG, "Key = " + entry.getKey() + "    value = " + entry.getValue());
}
```
#### ● 更新数据
当你需要更新数据时，只需要找到该键，然后写入新数据，重新save( )一下即可，代码如下：
```java
MicroKV.defaultMicroKV()
        .setKV("int", 86)
        .setKV("string", "Hello World !")
        .save();

MicroKV.customize("MyInfo")
        .setKV("age", 18)
        .save();

MicroKV.customize("Account", true)
        .setKV("token", "12345678")
        .setKV("updateAt", 1545383030666L)
        .save();
```
#### ● 查询数据
查询存储文件中某个键（key）是否存在，使用containsKV( )方法，代码如下：
```java
boolean hasBool = MicroKV.defaultMicroKV().containsKV("int");

boolean hasNickname = MicroKV.customize("MyInfo").containsKV("nickname");

boolean hasUsername = MicroKV.customize("Account").containsKV("username");
```
#### ● 校对数据
若开发者需要用一个值与存储文件中某个键值对比较是否相同，只需要使用comparisonKV( )方法，代码如下：
```java
boolean equal = MicroKV.defaultMicroKV().comparisonKV("int", 123);
```
实际应用场景，例如校对账号密码，代码如下：
```java
//用户输入的账号与密码
String inputUser = "Lisa";
String inputPwd = "12345678";

MicroKV kv = MicroKV.customize("Account", true);

if (kv.comparisonKV("username", inputUser) && kv.comparisonKV("password", inputPwd)){
    Log.d(TAG, "用户名和密码正确！");
}else {
    Log.d(TAG, "用户名或密码有误！");
}
```
#### ● 移除数据
移除某个键值对，使用removeKV( )方法进行移除，代码如下：
```java
MicroKV.defaultMicroKV().removeKV("int");

MicroKV.customize("MyInfo").removeKV("age");
```
需要移除多个键值对，可以使用removeKVs( )方法进行移除，代码如下：
```java
String[] strings = new String[]{"nickname", "address", "signature"};
MicroKV.customize("MyInfo").removeKVs(strings);
```
#### ● 清除数据
清除整个文件的数据，只需调用clear( )方法即可清空文件里的所有数据，代码如下：
```java
MicroKV.defaultMicroKV().clear();

MicroKV.customize("Account").clear();

MicroKV.customize("MyInfo").clear();
```
#### ● 删除文件
clear( )方法只会清除文件里的数据，但该文件还会留存在存储空间里，若想删除该文件，代码如下：
```java
MicroKV.defaultMicroKV().delete();

MicroKV.customize("Account").delete();

MicroKV.customize("MyInfo").delete();
```
#### ● 原生方法
MicroKV组件虽然是基于SharedPreferences封装，但是它还提供了获取SharedPreferences对象的方法，而且使用更加简单。

获取一个使用当前应用程序的包名来命名文件名的SharedPreferences对象，代码如下：
```java
SharedPreferences preferences = MicroKV.defaultMicroKV().getSharedPreferences();
SharedPreferences.Editor editor = preferences.edit();
```
获取一个自定义文件名的SharedPreferences对象，但不支持加密操作，代码如下：
```java
SharedPreferences preferences = MicroKV.customize("Data").getSharedPreferences();
SharedPreferences.Editor editor = preferences.edit();
```

## 混淆
```java
-keep class com.smailnet.microkv.** { *;}
```

## 感谢
感谢微信团队开源的 [MMKV](https://github.com/Tencent/MMKV) 组件提供api设计的参考。

感谢郭霖老师开源的 [LitePal](https://github.com/LitePalFramework/LitePal) 数据库操作框架提供AES加密设计的参考。

## License
```
Copyright 2018 Lake Zhang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```