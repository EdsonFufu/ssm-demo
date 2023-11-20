package com.example.ssm.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@Profile("local")
public class JpaPersisterConfig {

    @Value("${spring.profiles.active}") private String profile;

    private final Environment env;

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @Primary
    @SneakyThrows
    public DataSource dataSource() {
        if("local".equalsIgnoreCase(profile)) {
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName(env.getProperty("spring.datasource.driverClassName"));
            dataSourceBuilder.url(env.getProperty("spring.datasource.url"));
            dataSourceBuilder.username(env.getProperty("spring.datasource.username"));
            dataSourceBuilder.password(env.getProperty("spring.datasource.password"));
            return dataSourceBuilder.build();
        }else {
            JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
            bean.setJndiName(env.getProperty("spring.datasource.jndi-name"));
            bean.setProxyInterface(DataSource.class);
            bean.setLookupOnStartup(false);
            bean.afterPropertiesSet();
            return (DataSource) bean.getObject();
        }
    }
}