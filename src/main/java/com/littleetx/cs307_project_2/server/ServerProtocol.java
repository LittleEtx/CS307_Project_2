package com.littleetx.cs307_project_2.server;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import com.littleetx.cs307_project_2.database.Verification;
import cs307.project2.interfaces.StaffInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerProtocol extends UnicastRemoteObject implements IServerProtocol {
    public ServerProtocol() throws RemoteException {
        super();
    }

    private final Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());

    @Override
    public int verify(String user, String password) {
        return verification.checkAuthority(user, password);
    }

    @Override
    public StaffInfo getStaffInfo(int id) {
        return GlobalQuery.getStaffInfo(id);
    }
}
