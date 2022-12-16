package com.littleetx.cs307_project_2.database;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
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
        JsonFactory jf = new JsonFactory();
        try (JsonParser jp = jf.createParser(file)) {
            ObjectMapper mapper = new ObjectMapper();
            info = mapper.readValue(jp, DatabaseLoginInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("Can not read database_login.json", e);
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

