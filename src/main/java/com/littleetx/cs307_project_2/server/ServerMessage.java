package com.littleetx.cs307_project_2.server;

import java.time.LocalDateTime;

public class ServerMessage {
    public static void print(String str) {
        LocalDateTime time = LocalDateTime.now();
        System.out.println("[" + time.toLocalDate() + " " + String.format("%02d", time.getHour())
                + ":" + String.format("%02d", time.getMinute())
                + ":" + String.format("%02d", time.getSecond()) + "]" + str);
    }
}
