package com.littleetx.cs307_project_2.server;

import java.time.LocalDateTime;

public class ServerMessage {
    public static void print(String str) {
        LocalDateTime time = LocalDateTime.now();
        System.out.println("[" + time.toLocalDate() + " " + time.getHour() + ":" + time.getMinute()
                + ":" + time.getSecond() + "]" + str);
    }
}
