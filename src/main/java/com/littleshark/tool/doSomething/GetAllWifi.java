package com.littleshark.tool.doSomething;

import com.littleshark.tool.Constant;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

//获取所有的wifi的信息的任务
public class GetAllWifi {
    private final Set<String> allWifiInfo = new TreeSet<>();

    /**
     * 获取所有的WiFi的ssid信息
     */
    private void getInfo() {
        BufferedReader bufferedReader = null;
        try {
            Process exec = Runtime.getRuntime().exec(Constant.GET_ALL_WIFI, null);
            InputStream inputStream = exec.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if (s.contains("SSID ") && !s.contains("BSSID")) {
                    String ss = s.substring(9);
                    if (!ss.equals("")) {
                        allWifiInfo.add(ss);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Set<String> getAllWifiInfo() {
        getInfo();
        return allWifiInfo;
    }
}
