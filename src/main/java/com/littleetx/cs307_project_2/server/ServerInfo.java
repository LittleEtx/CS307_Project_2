package com.littleetx.cs307_project_2.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public record ServerInfo(
        String host,
        int port
) {
    private static final String LOGIN_INFO_PATH = "server_info.json";

    public static ServerInfo getInfo() {
        ServerInfo info;
        JsonFactory jf = new JsonFactory();
        File file = new File(LOGIN_INFO_PATH);
        if (!file.exists()) {
            info = new ServerInfo("localhost", 1099);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("""
                        {
                          "host": "127.0.0.1",
                          "port": 1099
                        }
                        """);
                ServerMessage.print("Server info file created. Default host: " + info.host() + ", port: " + info.port());
            } catch (IOException e) {
                throw new RuntimeException("Can not create server_info.json", e);
            }
        } else {
            try {
                JsonParser jp = jf.createParser(file);
                ObjectMapper mapper = new ObjectMapper();
                info = mapper.readValue(jp, ServerInfo.class);
            } catch (IOException e) {
                throw new RuntimeException("Can not read server_info.json", e);
            }
        }
        return info;
    }

    public String getUrl() {
        return "rmi://" + host() + ":" + port() + "/server";
    }


}
