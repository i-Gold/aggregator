package com.company.aggregator.service;

import com.company.aggregator.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class UserInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UserInitializer.class);
    private final Map<String, DataSource> dataSources;
    private final Map<String, List<UserDTO>> userMap = Map.of(
            "primary_db", List.of(new UserDTO("example-user-id-1", "user-1", "User", "Userenko")),
            "secondary_db", List.of(new UserDTO("example-user-id-2", "user-2", "Testuser", "Testov"))
    );

    @Autowired
    public UserInitializer(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    @Override
    public void run(String... args) {
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            String dataSourceName = entry.getKey();
            DataSource dataSource = entry.getValue();

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            createUserTable(jdbcTemplate);

            logger.info("CREATING USERS for {}:", dataSourceName);

            List<UserDTO> users = userMap.get(dataSourceName);
            if (Objects.nonNull(users) && !users.isEmpty()) {
                for (UserDTO user : users) {
                    addUser(jdbcTemplate, user.getId(), user.getUsername(), user.getName(), user.getSurname());
                }
            }
        }
    }

    private void createUserTable(JdbcTemplate jdbcTemplate) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id VARCHAR(255) PRIMARY KEY, " +
                "username VARCHAR(255), " +
                "name VARCHAR(255), " +
                "surname VARCHAR(255)" +
                ")";
        jdbcTemplate.execute(createTableSQL);
    }

    private void addUser(JdbcTemplate jdbcTemplate, String id, String username, String name, String surname) {
        String insertUserSQL = "INSERT INTO users (id, username, name, surname) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO NOTHING";
        jdbcTemplate.update(insertUserSQL, id, username, name, surname);
    }
}


