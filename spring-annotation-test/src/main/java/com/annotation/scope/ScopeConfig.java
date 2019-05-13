package com.annotation.scope;

import com.annotation.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

//配置类
@Configuration
public class ScopeConfig {

    //容器注册一个bean，默认用方法名当作容器Id
    @Bean
    /*
     * Scope表示bean的作用域
     * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
     * @see ConfigurableBeanFactory#SCOPE_SINGLETON
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
     *
     * prototype多实例
     * singleton单实例(默认) 启动的时候执行完成
     * request同一次请求创建一次
     * session同一个session创建一次
     */
//    @Scope(value = "prototype")
    @Scope
    //懒加载，启动的时候不创建，用到的时候只创建一次
    @Lazy
    public Student student(){
        System.out.println("创建了一次实例........");
        return new Student("张三",18);
    }
}
