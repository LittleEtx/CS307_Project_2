package com.littleetx.cs307_project_2.client;

import com.littleetx.cs307_project_2.server.IServerProtocol;
import com.littleetx.cs307_project_2.server.ServerInfo;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientHelper {
    public static IServerProtocol getConnection()
            throws MalformedURLException, NotBoundException, RemoteException {
        return (IServerProtocol)
                Naming.lookup(ServerInfo.getInfo().getUrl());
    }
}
