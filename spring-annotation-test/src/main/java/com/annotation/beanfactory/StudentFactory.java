package com.annotation.beanfactory;

import com.annotation.model.Student;
import org.springframework.beans.factory.FactoryBean;

//工厂bean
public class StudentFactory implements FactoryBean<Student> {
    @Override
    public Student getObject() throws Exception {
        //创建返回容器里面
        System.out.println("创建了一次实例........");
        return new Student("beanfactory",0);
    }

    @Override
    public Class<?> getObjectType() {
        return Student.class;
    }
    //true 为单例
    //false 为多实例
    @Override
    public boolean isSingleton() {
        return false;
    }
}
