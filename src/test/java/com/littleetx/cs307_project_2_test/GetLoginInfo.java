package com.littleetx.cs307_project_2_test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class GetLoginInfo {
    private static final String LOGIN_INFO_PATH = "database_login.json";
    public record LoginInfo(
            int port,
            String host,
            String databaseName,
            String username,
            String password) {
    }

    public static LoginInfo getLoginInfo() {
        File file = new File(LOGIN_INFO_PATH);
        LoginInfo info ;
        JsonFactory jf = new JsonFactory();
        try (JsonParser jp = jf.createParser(file)) {
            ObjectMapper mapper = new ObjectMapper();
            info = mapper.readValue(jp, LoginInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("Can not read database_login.json", e);
        }
        return info;
    }

    public static String getUrl(LoginInfo info, boolean useSUSTC) {
        return "jdbc:postgresql://" + info.host() + ":" + info.port() +
                "/" + (useSUSTC ? "sustc" : info.databaseName());
    }
}
