package com.huang.entity;

import com.huang.annotation.MyComponent;
import com.huang.annotation.MyValue;

/**
 * @Auther: pc.huang
 * @Date: 2018/7/24 10:46
 * @Description:
 */
@MyComponent(scope = "prototype")
public class PrototypeUser {
    @MyValue("985")
    private Integer id;
    @MyValue("李四")
    private String name;
    @MyValue("6549")
    private String password;

    public PrototypeUser() {
        System.out.println("多例无参方法执行");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
