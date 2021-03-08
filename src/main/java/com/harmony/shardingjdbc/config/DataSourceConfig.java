package com.harmony.shardingjdbc.config;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @describe
 * @author: wangkuan
 * @create: 2021-03-08 16:00:22
 **/
@Configuration
public class DataSourceConfig {

    @Bean("dataSource")
    public DataSource createDataBase() throws IOException, SQLException {
        File yamlFile = new File("src/main/resources/application-sharding-jdbc.yml");
        DataSource dataSource = YamlShardingDataSourceFactory.createDataSource(yamlFile);
        return dataSource;
    }
}
