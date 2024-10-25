package com.company.aggregator.service;

import com.company.aggregator.config.DataSourceProperties;
import com.company.aggregator.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDataAggregator {

    private static final Logger logger = LoggerFactory.getLogger(UserDataAggregator.class);
    private final Map<String, DataSource> dataSources;
    private final DataSourceProperties dataSourceProperties;

    @Transactional(readOnly = true)
    public List<UserDTO> aggregateUsers(String username, String name, String surname) {
        List<UserDTO> users = new ArrayList<>();
        for (DataSourceProperties.DataSourceConfig config : dataSourceProperties.getDatabases()) {
            String tableName = config.getTable();

            if (StringUtils.isBlank(tableName)) {
                logger.error("Table name is null for datasource: {}", config.getName());
                return Collections.emptyList();
            }

            try (Connection conn = dataSources.get(config.getName()).getConnection();
                 Statement stmt = conn.createStatement()) {

                StringBuilder sql = new StringBuilder(String.format("SELECT * FROM %s WHERE 1=1", tableName));

                if (Objects.nonNull(username) && !username.isEmpty()) {
                    sql.append(String.format(" AND %s LIKE '%%%s%%'", config.getMapping().getUsername(), username));
                }
                if (Objects.nonNull(name) && !name.isEmpty()) {
                    sql.append(String.format(" AND %s LIKE '%%%s%%'", config.getMapping().getName(), name));
                }
                if (Objects.nonNull(surname) && !surname.isEmpty()) {
                    sql.append(String.format(" AND %s LIKE '%%%s%%'", config.getMapping().getSurname(), surname));
                }

                logger.info("Executing SQL: {}", sql);

                ResultSet rs = stmt.executeQuery(sql.toString());
                while (rs.next()) {
                    UserDTO user = new UserDTO(
                            rs.getString(config.getMapping().getId()),
                            rs.getString(config.getMapping().getUsername()),
                            rs.getString(config.getMapping().getName()),
                            rs.getString(config.getMapping().getSurname()));
                    users.add(user);
                }
            } catch (Exception e) {
                logger.error("Error while aggregating users from data source: {}", config.getName(), e);
            }
        }
        return users;
    }

}



