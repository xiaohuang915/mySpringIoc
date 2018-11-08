package com.huang.applicationContext;

import com.huang.annotation.MyComponent;
import com.huang.annotation.MyValue;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: pc.huang
 * @Date: 2018/7/24 10:49
 * @Description:
 */
public class AnnotationConfigApplicationContext {
    //存储类定义对象
    private Map<String, Class<?>> beanDefinationFactory = new ConcurrentHashMap<>();
    //存储单例对象
    private Map<String, Object> singletonbeanFactory = new ConcurrentHashMap<>();

    /**
     * @Description:
     * @param: [packageName] 需要扫描加载的包名
     * @return:
     * @auther: pc.huang
     * @date:
     */
    public AnnotationConfigApplicationContext(String packageName) {
        scanPackage(packageName);
    }

    /**
     * @Description: 扫描包
     * @param: [packageName] 包名
     * @return: void
     * @auther: pc.huang
     * @date: 2018/7/24 10:54
     */
    private void scanPackage(final String packageName) {
        String packDir = packageName.replaceAll("\\.", "/");
        URL url = this.getClass().getClassLoader().getResource(packDir);
        File file = new File(url.getFile());
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fName = file.getName();
                if (file.isDirectory()) {
                    scanPackage(packageName + "." + fName);
                } else {
                    if (fName.endsWith(".class")) {
                        return true;
                    }
                }
                return false;
            }
        });
        //遍历所有文件实例化
        for (File f : files) {
            //获取文件名
            String fName = f.getName();
            //获取去除.class之后的文件名
            fName = fName.substring(0, fName.lastIndexOf("."));
            //将文件名首字母小写
            String key = toLowerFirstWord(fName);
            //构建全类名
            String packClsName = packageName + "." + fName;
            try {
                Class<?> aClass = Class.forName(packClsName);
                if (aClass.isAnnotationPresent(MyComponent.class)) {
                    beanDefinationFactory.put(key, aClass);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * @Description: 根据bean Id获取容器中的对象
     * @param: [beanId]
     * @return: java.lang.Object
     * @auther: pc.huang
     * @date: 2018/7/24 11:16
     */
    public Object getBean(String beanId) {
        Class<?> aClass = beanDefinationFactory.get(beanId);
        MyComponent annotation = aClass.getAnnotation(MyComponent.class);
        String scope = annotation.scope();
        try {
            if ("singleton".equals(scope)) {
                if (singletonbeanFactory.get(beanId) == null) {
                    Object instance = aClass.newInstance();
                    setFieldValues(aClass, instance);
                    singletonbeanFactory.put(beanId, instance);
                }
                return singletonbeanFactory.get(beanId);
            }
            if ("prototype".equals(scope)) {
                Object instance = aClass.newInstance();
                setFieldValues(aClass, instance);
                return instance;
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 根据传入的class对象强转
     * @param: [beanId, c]
     * @return: T
     * @auther: pc.huang
     * @date: 2018/7/24 11:25
     */
    public <T> T getBean(String beanId, Class<T> c) {
        return (T) getBean(beanId);
    }

    /**
     * @Description: 设置属性值
     * @param: [aClass, instance]
     * @return: void
     * @auther: pc.huang
     * @date: 2018/7/24 11:21
     */
    public void setFieldValues(Class<?> cls, Object obj) {
        //获取类中所有的成员属性
        Field[] fields = cls.getDeclaredFields();
        //遍历所有属性
        for (Field field : fields) {
            //如果此属性有MyValue注解修饰，对其进行操作
            if (field.isAnnotationPresent(MyValue.class)) {
                //获取属性名
                String fieldName = field.getName();
                //获取注解中的值
                String value = field.getAnnotation(MyValue.class).value();
                //获取属性所定义的类型
                String type = field.getType().getName();
                //将属性名改为以大写字母开头，如：id改为ID，name改为Name
                fieldName = String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                //set方法名称，如：setId,setName...
                String setterName = "set" + fieldName;
                try {
                    //根据方法名称和参数类型获取对应的set方法对象
                    Method method = cls.getDeclaredMethod(setterName, field.getType());
                    //判断属性类型，如类型不一致，则转换类型后调用set方法为属性赋值
                    if ("java.lang.Integer".equals(type) || "int".equals(type)) {
                        int intValue = Integer.valueOf(value);
                        method.invoke(obj, intValue);
                    } else if ("java.lang.String".equals(type)) {
                        method.invoke(obj, value);
                    }
                    //作为测试，仅判断Integer和String类型，其它类型同理
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Description: 字符串首字母小写
     * @param: [name]
     * @return: java.lang.String
     * @auther: pc.huang
     * @date: 2018/7/19 15:59
     */
    private String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

    /**
     * @Description: 销毁方法，释放资源
     * @param: []
     * @return: void
     * @auther: pc.huang
     * @date: 2018/7/24 11:31
     */
    public void close() {
        beanDefinationFactory.clear();
        beanDefinationFactory = null;
        singletonbeanFactory.clear();
        singletonbeanFactory = null;
    }
}
