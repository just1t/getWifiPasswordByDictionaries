package com.littleshark.tool.doSomething;

import com.littleshark.tool.Constant;
import com.littleshark.tool.pool.ThePool;

import java.io.*;

//将字典中的密码放入池对象中
public class GetPasswordFromDictionary {

    private final ThePool<String> passwordPool=new ThePool<>();

    /**
     * 将字典中的信息存入池对象中
     */
    public void addToPool(){
        BufferedReader bufferedReader=null;
        try {
            //指定字典的位置
            String dictionaryPath = Constant.DICTIONARY_PATH;
            bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryPath),"gbk"));
            String s="";
            while((s= bufferedReader.readLine())!=null&&!s.equals("")){
                passwordPool.addToPool(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ThePool<String> getPasswordPool() {
        return passwordPool;
    }
}
