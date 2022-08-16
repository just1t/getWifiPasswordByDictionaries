package com.littleshark.tool.application;

import com.littleshark.tool.Constant;
import com.littleshark.tool.doSomething.GetAllWifi;
import com.littleshark.tool.doSomething.GetPasswordFromDictionary;
import com.littleshark.tool.task.PingTask;
import com.littleshark.tool.task.SaveConfigTask;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        GetAllWifi getAllWifiTask = new GetAllWifi();
        Set<String> allWifiInfo = getAllWifiTask.getAllWifiInfo();
        String[] strings = allWifiInfo.toArray(new String[allWifiInfo.size()]);
        Thread ping = new Thread(new PingTask());
        ping.start();
        for (int i = 0; i < allWifiInfo.size(); i++) {
            executorService.execute(new SaveConfigTask(strings[i], new GetPasswordFromDictionary()));
        }
        executorService.shutdown();
        //判断线程池中的任务都已经完成了
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Constant.FLOG = true;
        while (ping.isAlive()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        File file=new File(Constant.SAVE_DISK_PATH);
        File[] files = file.listFiles();
        System.out.println("正在删除生成的文件...");
        while (Objects.requireNonNull(file.listFiles()).length!=0){
            Objects.requireNonNull(file.listFiles())[0].delete();
        }
        file.delete();
        System.out.println("删除完成！");
    }
}
