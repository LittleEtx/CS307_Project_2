package com.littleetx.cs307_project_2.server;

import com.littleetx.cs307_project_2.database.database_type.CityInfo;
import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
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

    boolean checkItem(int id, String itemName, boolean isSuccess) throws RemoteException;

    Map<TaxInfo.Key, TaxInfo.Value> getTaxRates(String city) throws RemoteException;

    //company manager methods
    Map<TaxInfo.Key, TaxInfo.Value> getTaxRates() throws RemoteException;

    Map<String, ItemFullInfo> getCompanyItems(int id, CompanyManager.GetItemType type) throws RemoteException;

    Map<Integer, StaffInfo> getCompanyCouriers(int id) throws RemoteException;

    Map<String, ShipInfo> getCompanyShips(int id, CompanyManager.GetShipType type) throws RemoteException;

    Map<String, ContainerInfo> getContainers(int id, CompanyManager.GetContainerType type) throws RemoteException;

    boolean loadItemToContainer(int id, String itemName, String containerCode) throws RemoteException;

    boolean loadContainerToShip(int id, String shipName, String containerCode) throws RemoteException;

    boolean shipStartSailing(int id, String shipName) throws RemoteException;

    boolean unloadItem(int id, String itemName) throws RemoteException;

    boolean itemWaitForChecking(int id, String itemName) throws RemoteException;

    //SUSTC manager methods
    Map<String, ItemFullInfo> getAllItems(int id) throws RemoteException;

    Map<Integer, StaffInfo> getAllStaffs(int id) throws RemoteException;

    Map<String, ShipInfo> getAllShips(int id) throws RemoteException;

    Map<String, ContainerInfo> getAllContainers(int id) throws RemoteException;

    //public methods

    Map<Integer, String> getAllCompanies() throws RemoteException;

    Map<Integer, CityInfo> getAllCities() throws RemoteException;




}
