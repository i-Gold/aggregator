package com.company.aggregator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DataSourceProperties {

    private List<DataSourceConfig> databases;

    @Data
    public static class DataSourceConfig {
        private String name;
        private String strategy;
        private String url;
        private String username;
        private String password;
        private String table;
        private MappingConfig mapping;
    }

    @Data
    public static class MappingConfig {
        private String id;
        private String username;
        private String name;
        private String surname;
    }
}


