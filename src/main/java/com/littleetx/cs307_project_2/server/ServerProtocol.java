package com.littleetx.cs307_project_2.server;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import com.littleetx.cs307_project_2.database.user.Courier;
import main.interfaces.ItemInfo;
import main.interfaces.StaffInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

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

    @Override
    public void newItem(int id, ItemInfo item) throws RemoteException {

    }

    @Override
    public void updateItemState(int id, String itemName) throws RemoteException {

    }

    @Override
    public Map<String, ItemInfo> getCourierItems(int id, Courier.GetItemType type) throws RemoteException {
        ServerMessage.print("courier " + id + " get items: " + type);
        return verification.getUser(id, Courier.class).getAllItems(type);
    }

    @Override
    public Map<String, ItemInfo> getItemsInPort(int id) throws RemoteException {
        return null;
    }

    @Override
    public void checkItem(int id, String itemName, boolean isSuccess) throws RemoteException {

    }

    @Override
    public Map<TaxInfo.Key, TaxInfo.Value> getTaxRates() {
        return null;
    }

    @Override
    public Map<String, ItemInfo> getCompanyItems(int id) {
        return null;
    }
}
