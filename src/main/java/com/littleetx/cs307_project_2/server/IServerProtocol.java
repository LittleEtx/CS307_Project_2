package com.littleetx.cs307_project_2.server;

import com.littleetx.cs307_project_2.database.database_type.CityInfo;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.database.user.SeaportOfficer;
import main.interfaces.ContainerInfo;
import main.interfaces.ItemInfo;
import main.interfaces.ShipInfo;
import main.interfaces.StaffInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public interface IServerProtocol extends Remote {
    int verify(String username, String password) throws RemoteException;

    StaffInfo getStaffInfo(int id) throws RemoteException;

    //courier methods
    Set<String> getItemTypes() throws RemoteException;

    boolean newItem(int id, String name, String type, int price,
                    int delivery_city, int export_city, int import_city) throws RemoteException;

    boolean updateItemState(int id, String itemName) throws RemoteException;

    boolean takeItem(int id, String itemName) throws RemoteException;

    Map<String, ItemInfo> getCourierItems(int id, Courier.GetItemType type) throws RemoteException;

    //seaport officer methods
    Map<String, ItemInfo> getItemsInPort(int id, SeaportOfficer.GetItemType type) throws RemoteException;

    void checkItem(int id, String itemName, boolean isSuccess) throws RemoteException;

    Map<TaxInfo.Key, TaxInfo.Value> getTaxRates(String city) throws RemoteException;

    //company manager methods
    Map<TaxInfo.Key, TaxInfo.Value> getTaxRates() throws RemoteException;

    Map<String, ItemInfo> getCompanyItems(int id) throws RemoteException;

    //SUSTC manager methods
    Map<String, ItemInfo> getAllItems(int id) throws RemoteException;

    Map<Integer, StaffInfo> getAllStaffs(int id) throws RemoteException;

    //public methods

    Map<Integer, String> getAllCompanies() throws RemoteException;

    Map<String, ShipInfo> getAllShips(int id) throws RemoteException;

    Map<Integer, CityInfo> getAllCities() throws RemoteException;

    Map<String, ContainerInfo> getAllContainers(int id) throws RemoteException;


}
