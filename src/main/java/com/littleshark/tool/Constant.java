package com.littleshark.tool;

import com.littleshark.tool.pojo.PingObject;
import com.littleshark.tool.pool.ThePool;

public class Constant {
    // 列出所有可用wifi
    public static final String GET_ALL_WIFI="netsh wlan show networks mode=bssid";
    // 添加配置文件
    public static final String ADD_CONFIG_FILE="netsh wlan add profile filename=";
    // 连接wifi
    public static final String CONNECT_WIFI="netsh wlan connect name=";
    // 删除配置文件
    public static final String DELETE_CONFIG_FILE="netsh wlan delete profile name=";
    // 指定字典的位置
    public static final String DICTIONARY_PATH="D:\\getWifiPasswordByDictionaries\\target\\wifi.txt";
    // 将每一个wifi的xml文件保存的位置
    public static final String SAVE_DISK_PATH="D:\\getWifiPasswordByDictionaries\\wifi_config";
    // 进行Ping测试的网站地址
    public static final String PING_TEST_PATH="ping www.baidu.com";
    // 将Ping成功的PingObject对象进行保存
    public static final String THE_SUCCESS_PATH="D:\\getWifiPasswordByDictionaries\\success.txt";
    //将保存之后的封装成为一个对象进行保存
    public static final ThePool<PingObject> POOL=new ThePool<>();
    //
    public static  boolean FLOG=false;
}
