package com.example.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.druid")
    public DruidDataSource druidDataSource(){

        DruidDataSource druidDataSource = new DruidDataSource();
        //Lists.newArrayList()  相当于 new ArrayList()  goole工具包提供
        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return druidDataSource;
    }

    //配置过滤的数据
    @Bean
    public StatFilter statFilter(){

        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setSlowSqlMillis(5);
        statFilter.setMergeSql(true);
        return statFilter;
    }

    //访问路径
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        //localhost:8080/wxdc/druid
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }
}
