package ru.kostyanoy.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceProvider {

    private static final Logger log = LoggerFactory.getLogger(DataSourceProvider.class);

    private static final String CONFIG_FILE_NAME = "/application.properties";

    private static final DataSourceProvider INSTANCE = new DataSourceProvider();

    private final DataSource dataSource;

    private DataSourceProvider() {
        Properties properties = new Properties();
        try (InputStream propertiesStream = DataSourceProvider.class.getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(propertiesStream);
        } catch (IOException e) {
            log.error("Could not load properties", e);
            throw new RuntimeException(e);
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Postgres driver not found in classpath", e);
            throw new RuntimeException(e);
        }

        dataSource = new HikariDataSource(new HikariConfig(properties));
    }

    public static DataSource getDataSource() {
        return INSTANCE.dataSource;
    }
}
