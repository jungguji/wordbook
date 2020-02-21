package com.jgji.spring.domain.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "com.jgji.spring.domain.word.repository")
public class MyBatisConfig {
    
    @Bean
    public SqlSessionFactory sqlSessionFactory (DataSource dataSource) throws Exception {
         SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
         
         bean.setDataSource(dataSource);
         bean.setTypeAliasesPackage("com.jgji.spring.domain.word.model");
         bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
         
         Properties prop = new Properties();
         prop.setProperty("mapUnderscoreToCamelCase", "true");
         bean.setConfigurationProperties(prop);
         return bean.getObject();
    }
    
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
