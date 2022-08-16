package com.littleshark.tool.task;

import com.littleshark.tool.Constant;
import com.littleshark.tool.doSomething.GetPasswordFromDictionary;
import com.littleshark.tool.pojo.PingObject;
import com.littleshark.tool.pool.ThePool;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


/**
 * 将配置文件进行保存到磁盘中，并将其添加到接口中
 */
public class SaveConfigTask implements Runnable {

    //获取ssid
    private final String ssid;
    //获取密码
    private final GetPasswordFromDictionary getPasswordFromDictionary;

    public SaveConfigTask(String ssid, GetPasswordFromDictionary getPasswordFromDictionary) {
        this.ssid = ssid;
        this.getPasswordFromDictionary = getPasswordFromDictionary;
    }

    @Override
    public void run() {
        saveInDisk();
    }


    /**
     * 将wifi配置文件保存到磁盘中
     */
    private void saveInDisk() {
        FileOutputStream fileOutputStream = null;
        //进行加载密码信息
        getPasswordFromDictionary.addToPool();
        ThePool<String> passwordPool = getPasswordFromDictionary.getPasswordPool();
        try {
            File dir = new File(Constant.SAVE_DISK_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (ssid == null) {
                return;
            }
            while (!passwordPool.isEmpty()) {
                String password = passwordPool.getOne();
                String wifiXML = getWifiXML(ssid, password);
                try {
                    fileOutputStream = new FileOutputStream(Constant.SAVE_DISK_PATH + "\\" + ssid + "_" + password + ".xml");
                    FileChannel channel = fileOutputStream.getChannel();
                    FileLock fileLock = channel.tryLock();
                    if (fileLock!=null){
                        fileOutputStream.write(wifiXML.getBytes("gbk"));
                        fileOutputStream.flush();
                        fileLock.release();
                    }
                } catch (Exception ignored) {
                }
                addThePingObject(ssid, password);
            }
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取wifi的xml文件
     * 每一个wifi配置文件的name标签值为ssid_password
     */
    private String getWifiXML(String ssid, String password) {
        String name = ssid + "_" + password;
        return "<?xml version=\"1.0\"?>\n" +
                "<WLANProfile xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v1\">\n" +
                "    <name>" + name + "</name>\n" +
                "    <SSIDConfig>\n" +
                "        <SSID>\n" +
                "            <name>" + ssid + "</name>\n" +
                "        </SSID>\n" +
                "    </SSIDConfig>\n" +
                "    <connectionType>ESS</connectionType>\n" +
                "    <connectionMode>manual</connectionMode>\n" +
                "    <MSM>\n" +
                "        <security>\n" +
                "            <authEncryption>\n" +
                "                <authentication>WPA2PSK</authentication>\n" +
                "                <encryption>AES</encryption>\n" +
                "                <useOneX>false</useOneX>\n" +
                "            </authEncryption>\n" +
                "            <sharedKey>\n" +
                "                <keyType>passPhrase</keyType>\n" +
                "                <protected>false</protected>\n" +
                "                <keyMaterial>" + password + "</keyMaterial>\n" +
                "            </sharedKey>\n" +
                "        </security>\n" +
                "    </MSM>\n" +
                "    <MacRandomization xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v3\">\n" +
                "        <enableRandomization>false</enableRandomization>\n" +
                "    </MacRandomization>\n" +
                "</WLANProfile>";
    }

    public void addThePingObject(String ssid, String password) {
        String name = ssid + "_" + password;
        Constant.POOL.addToPool(new PingObject(name, ssid, password));
    }
}
