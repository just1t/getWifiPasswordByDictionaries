package com.littleshark.tool.task;

import com.littleshark.tool.Constant;
import com.littleshark.tool.pojo.PingObject;
import com.littleshark.tool.pool.ThePool;

import java.io.*;

//进行连接在进行做Ping测试的任务
public class PingTask implements Runnable {

    private final ThePool<PingObject> pool=Constant.POOL;

    /**
     * 进行Ping测试
     */
    public boolean pingTest(){
        BufferedReader bufferedReader=null;
        try {
            Process exec = Runtime.getRuntime().exec(Constant.PING_TEST_PATH);
            bufferedReader=new BufferedReader(new InputStreamReader(exec.getInputStream(),"gbk"));
            return !bufferedReader.readLine().contains("请求找不到主机");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 进行连接测试
     * @param one 连接的对象
     */
    public boolean connectionTest(PingObject one){
        BufferedReader bufferedReader=null;
        try {
            Process exec = Runtime.getRuntime().exec(Constant.CONNECT_WIFI + one.getName());
            bufferedReader=new BufferedReader(new InputStreamReader(exec.getInputStream(),"gbk"));
            return bufferedReader.readLine().replaceAll("》", "").contains("成功");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 将wifi信息存放到无线接口中
     */
    public boolean saveInInterface(PingObject one){
        BufferedReader bufferedReader=null;
        try {
            Process exec = Runtime.getRuntime().exec(Constant.ADD_CONFIG_FILE + new File(Constant.SAVE_DISK_PATH+"\\"+one.getName()+".xml"));
            bufferedReader=new BufferedReader(new InputStreamReader(exec.getInputStream(),"gbk"));
            return bufferedReader.readLine().contains("已将配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 将Ping成功的对象进行保存到磁盘中
     */
    public void saveThePassword(PingObject one){
        FileOutputStream fileOutputStream=null;
        File success=new File(Constant.THE_SUCCESS_PATH);
        if (!success.getParentFile().exists()){
            success.getParentFile().mkdirs();
        }
        if (!success.exists()){
            try {
                success.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String s = "=================================================================\nssid=" + one.getSsid() + ", password=" + one.getPassword() + "\n";
        try {
            fileOutputStream=new FileOutputStream(success,true);//进行追加
            fileOutputStream.write(s.getBytes("gbk"));
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
     * 将Ping对象从接口中进行移除
     */
    public void removeFormInterface(PingObject one){
        BufferedReader bufferedReader=null;
        try {
            Process exec = Runtime.getRuntime().exec(Constant.DELETE_CONFIG_FILE + one.getName());
            bufferedReader=new BufferedReader(new InputStreamReader(exec.getInputStream(),"gbk"));
            System.out.println("还有"+pool.getSize()+"需要进行测试！");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 这个方法进行反复的调用，判断的依据是其他线程是否已经完成了他们的任务，如果完成了就将程序进行退出，如果没有完成就继续调用这个方法
     */
    @Override
    public void run() {
        while (true){
            while (!pool.isEmpty()){
                PingObject one = pool.getOne();
                boolean b = saveInInterface(one);
                if (b){
                    boolean b1 = connectionTest(one);
                    if (b1){
                        try {
                            Thread.sleep(550);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean b2 = pingTest();
                        if (b2){
                            //将其保存到磁盘中
                            saveThePassword(one);
                        }
                    }
                }
                removeFormInterface(one);
            }
            try {
                Thread.sleep(100);
                if (Constant.FLOG){
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
