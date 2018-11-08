package com.huang.test;

import com.huang.applicationContext.AnnotationConfigApplicationContext;
import com.huang.entity.PrototypeUser;
import com.huang.entity.SingletonUser;

/**
 * @Auther: pc.huang
 * @Date: 2018/7/24 11:33
 * @Description:
 */
public class IocTest {
    public static void main(String[] args){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.huang.entity");
        SingletonUser singletonUser = (SingletonUser) applicationContext.getBean("singletonUser");
        System.out.println(singletonUser);
        SingletonUser singletonUser1 = applicationContext.getBean("singletonUser",SingletonUser.class);
        System.out.println(singletonUser1);
        PrototypeUser prototypeUser = (PrototypeUser) applicationContext.getBean("prototypeUser");
        System.out.println(prototypeUser);
        PrototypeUser prototypeUser1 = applicationContext.getBean("prototypeUser",PrototypeUser.class);
        System.out.println(prototypeUser1);
        applicationContext.close();
    }
}
