package com.littleetx.cs307_project_2.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        ServerInfo info = ServerInfo.getInfo();
        try {
            LocateRegistry.createRegistry(info.port());
            IServerProtocol server = new ServerProtocol();
            Naming.rebind(info.getUrl(), server);
            ServerMessage.print("Server opened at " + info.host() + ":" + info.port());
        } catch (Exception e) {
            ServerMessage.print("Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
