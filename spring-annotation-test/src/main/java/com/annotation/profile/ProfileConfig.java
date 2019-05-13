package com.annotation.profile;

import com.annotation.model.DataSourceBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;


/*
@Profile 根据环境参数加载指定的组件到容器，不指定环境名只有默认default模式才能注册
 */
@Configuration
@PropertySource("classpath:/db.properties")
public class ProfileConfig implements EmbeddedValueResolverAware {

    @Value("${db.name}")
    private String name;

    private String url;
    @Value("${db.drive}")
    private String driveName;

    @Profile("test")
    @Bean
    public DataSourceBean testDataSource(@Value("${db.pwd}") String pwd) {
        DataSourceBean dataSourceBean = new DataSourceBean();
        dataSourceBean.setName(name);
        dataSourceBean.setDriveName(driveName);
        dataSourceBean.setPwd(pwd);
        dataSourceBean.setUrl(url);
        return dataSourceBean;
    }

    @Profile("dev")
    @Bean
    public DataSourceBean devDataSource(@Value("${db.pwd}") String pwd) {
        DataSourceBean dataSourceBean = new DataSourceBean();
        dataSourceBean.setName(name);
        dataSourceBean.setDriveName(driveName);
        dataSourceBean.setPwd(pwd);
        dataSourceBean.setUrl(url);
        return dataSourceBean;
    }

    @Profile("prod")
    @Bean
    public DataSourceBean prodDataSource(@Value("${db.pwd}") String pwd) {
        DataSourceBean dataSourceBean = new DataSourceBean();
        dataSourceBean.setName(name);
        dataSourceBean.setDriveName(driveName);
        dataSourceBean.setPwd(pwd);
        dataSourceBean.setUrl(url);
        return dataSourceBean;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.url = resolver.resolveStringValue("${db.url}");
    }
}
