package com.company.aggregator.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public Map<String, DataSource> dataSources() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        for (DataSourceProperties.DataSourceConfig config : dataSourceProperties.getDatabases()) {
            DataSource dataSource = DataSourceBuilder.create()
                    .url(config.getUrl())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .driverClassName("org.postgresql.Driver")
                    .build();

            dataSourceMap.put(config.getName(), dataSource);
        }
        return dataSourceMap;
    }
}

