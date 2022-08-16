package com.littleshark.tool.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;


//需要进行测试的对象
@AllArgsConstructor
@Data
public class PingObject {
    private String name;
    private String ssid;
    private String password;
}
