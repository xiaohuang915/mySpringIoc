package com.huang.entity;

import com.huang.annotation.MyComponent;
import com.huang.annotation.MyValue;

/**
 * @Auther: pc.huang
 * @Date: 2018/7/24 10:43
 * @Description:
 */
@MyComponent(scope = "singleton")
public class SingletonUser {
    @MyValue("211")
    private Integer id;
    @MyValue("张三")
    private String name;
    @MyValue("9527")
    private String password;

    public SingletonUser() {
        System.out.println("单例无参方法执行");
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
