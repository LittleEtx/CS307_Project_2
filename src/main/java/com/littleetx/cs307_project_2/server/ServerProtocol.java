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
    private StaffInfo staffInfo = null;

    @Override
    public boolean verify(String user, String password) {
        try {
            //convert to name if is id
            user = GlobalQuery.getStaffName(Integer.parseInt(user));
        } catch (NumberFormatException ignore) {
        }
        staffInfo = verification.checkAuthority(user, password);
        return staffInfo != null;
    }

    @Override
    public StaffInfo getStaffInfo() {
        if (staffInfo == null) {
            throw new IllegalStateException("You must verify first!");
        }
        return staffInfo;
    }
}
