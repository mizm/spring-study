package com.miz.study.ch1.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import javax.sql.DataSource;
import java.sql.DriverManager;

//@Configuration
public class DaoFactory {
//    @Bean
//    public DataSource dataSource() {
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();
//        dataSource.setDriverClass(org.h2.Driver);
//        dataSource.setUrl("jdbc:h2:~/test");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//
//        return dataSource;
//    }
}