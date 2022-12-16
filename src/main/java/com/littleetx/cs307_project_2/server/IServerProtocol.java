package com.littleetx.cs307_project_2.server;

import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import com.littleetx.cs307_project_2.database.user.Courier;
import main.interfaces.ItemInfo;
import main.interfaces.StaffInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IServerProtocol extends Remote {
    int verify(String username, String password) throws RemoteException;

    StaffInfo getStaffInfo(int id) throws RemoteException;

    //courier methods
    void newItem(int id, ItemInfo item) throws RemoteException;

    void updateItemState(int id, String itemName) throws RemoteException;

    Map<String, ItemInfo> getCourierItems(int id, Courier.GetItemType type) throws RemoteException;

    //seaport officer methods
    Map<String, ItemInfo> getItemsInPort(int id) throws RemoteException;

    void checkItem(int id, String itemName, boolean isSuccess) throws RemoteException;

    //company manager methods
    Map<TaxInfo.Key, TaxInfo.Value> getTaxRates() throws RemoteException;

    Map<String, ItemInfo> getCompanyItems(int id) throws RemoteException;

}