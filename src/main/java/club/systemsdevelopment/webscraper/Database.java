/*
 * @source: https://github.com/systemsdevelopmentclub/web-scraper
 * @license: https://www.gnu.org/licenses/lgpl.txt
 * @contact: https://www.stuorg.iastate.edu/site/sdc/information
 * Copyright (c) 2018 Systems Development Club
 * This file is part of SDC Search.
 * SDC Search is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * SDC Search is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with SDC Search.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.systemsdevelopment.webscraper;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.SQLException;

public class Database {
    private static final HikariDataSource HIKARI_DATA_SOURCE;
    private static final QueryRunner QUERY_RUNNER;
    static {
        String username = Configs.getDatabaseUser();
        String password = Configs.getDatabasePassword();
        String serverIP = Configs.getDatabaseHost();
        Integer serverPort = Configs.getDatabasePort();
        String dbName = Configs.getDatabaseName();
        try {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setDatabaseName(dbName);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setServerName(serverIP);
            dataSource.setPort(serverPort);
            dataSource.setZeroDateTimeBehavior("convertToNull");
            dataSource.setUseCompression(false);
            dataSource.setDisconnectOnExpiredPasswords(true);
            dataSource.setCharacterEncoding("utf8mb4");
            dataSource.setServerTimezone("UTC");
            dataSource.setGenerateSimpleParameterMetadata(true);
        } catch (SQLException e) {
            System.err.println("Unable to load data source");
            e.printStackTrace();
        }
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        config.setJdbcUrl("jdbc:mariadb://" + serverIP + ":" + serverPort + "/" + dbName + "?characterEncoding=utf8mb4");
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        HIKARI_DATA_SOURCE = new HikariDataSource(config);
        QUERY_RUNNER = new QueryRunner(HIKARI_DATA_SOURCE);
        query("SET NAMES utf8mb4");// Allows full unicode compatibility.
        // todo load DB connection
    }

    public static <E> E select(String sql, ResultSetHandler<E> handler) {
        try {
            return QUERY_RUNNER.query(sql, handler);
        } catch (SQLException e) {
            throw new RuntimeException("SQL error selecting: " + e.getErrorCode() + " " + sql, e);
        }
    }

    public static void query(String sql) {
        try {
            QUERY_RUNNER.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException("SQL error querying: " + e.getErrorCode() + " " + sql, e);
        }
    }

    public static void insert(String sql) {
        try {
            QUERY_RUNNER.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException("SQL error inserting: " + e.getErrorCode() + " " + sql, e);
        }
    }


    // todo add sanitation method
    // todo add shortcut methods relevant for the project
}
