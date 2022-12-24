package com.littleetx.cs307_project_2.database;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littleetx.cs307_project_2.server.ServerMessage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public record DatabaseLoginInfo(
        int port,
        String host,
        String databaseName,
        String username,
        String password
) {
    private static final String LOGIN_INFO_PATH = "database_login.json";

    public static DatabaseLoginInfo getLoginInfo() {
        File file = new File(LOGIN_INFO_PATH);
        DatabaseLoginInfo info;
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                info = new DatabaseLoginInfo(3306,
                        "localhost", "sustc", "root", "root");
                writer.write("""
                        {
                          "host": "localhost",
                          "port": 5432,
                          "databaseName": "sustc",
                          "username": "root",
                          "password": "root"
                        }
                        """);
                ServerMessage.print("Database login info file created. Default port: " + info.port() + ", host: " + info.host() + ", database name: " + info.databaseName() + ", username: " + info.username() + ", password: " + info.password());
            } catch (IOException e) {
                throw new RuntimeException("Cannot create database_login.json", e);
            }
        } else {
            JsonFactory jf = new JsonFactory();
            try (JsonParser jp = jf.createParser(file)) {
                ObjectMapper mapper = new ObjectMapper();
                info = mapper.readValue(jp, DatabaseLoginInfo.class);
            } catch (IOException e) {
                throw new RuntimeException("Can not read database_login.json", e);
            }

        }
        return info;
    }

    public String getUrl() {
        return getUrl(getDatabase());
    }

    public String getDatabase() {
        return host + ":" + port + "/" + databaseName;
    }

    public static String getUrl(String database) {
        return "jdbc:postgresql://" + database;
    }
}

