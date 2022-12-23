package com.littleetx.cs307_project_2.server;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.database_type.CityInfo;
import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.database.user.SUSTCManager;
import com.littleetx.cs307_project_2.database.user.SeaportOfficer;
import main.interfaces.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerProtocol extends UnicastRemoteObject implements IServerProtocol {
    public ServerProtocol() throws RemoteException {
        super();
    }

    private final Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());

    @Override
    public int verify(String user, String password) {
        int verifyID = verification.checkAuthority(user, password);
        ServerMessage.print("attempt log in: " + user + " " + password
                + ", result: " + (verifyID > 0 ? "success" : "fail"));
        return verifyID;
    }

    @Override
    public StaffInfo getStaffInfo(int id) {
        ServerMessage.print("user " + id + " get staff info");
        return GlobalQuery.getStaffInfo(id);
    }

    @Override
    public boolean newItem(int id, String name, String type, int price,
                           int delivery_city, int export_city, int import_city) throws RemoteException {
        ServerMessage.print("courier " + id + " created new item: " + name);
        return verification.getUser(id, Courier.class)
                .newItem(name, type, price, delivery_city, export_city, import_city);
    }

    @Override
    public boolean takeItem(int id, String itemName) throws RemoteException {
        ServerMessage.print("courier " + id + " took item: " + itemName);
        return verification.getUser(id, Courier.class)
                .setItemState(itemName, ItemState.FromImportTransporting);
    }

    @Override
    public Set<String> getItemTypes() throws RemoteException {
        ServerMessage.print("user get item types");
        return GlobalQuery.getItemTypes();
    }

    @Override
    public boolean updateItemState(int id, String itemName) throws RemoteException {
        ServerMessage.print("courier " + id + " updated item: " + itemName);
        return verification.getUser(id, Courier.class).updateItemState(itemName);
    }

    @Override
    public Map<String, ItemInfo> getCourierItems(int id, Courier.GetItemType type) throws RemoteException {
        ServerMessage.print("courier " + id + " get items: " + type);
        return verification.getUser(id, Courier.class).getAllItems(type);
    }

    @Override
    public Map<String, ItemInfo> getItemsInPort(int id, SeaportOfficer.GetItemType type) throws RemoteException {
        ServerMessage.print("seaport officer " + id + " get items: " + type);
        return verification.getUser(id, SeaportOfficer.class).getAllItemsAtPort(type);
    }

    @Override
    public void checkItem(int id, String itemName, boolean isSuccess) throws RemoteException {
        ServerMessage.print("seaport officer " + id + " check item " + itemName
                + " to " + (isSuccess ? "pass" : "fail"));
        verification.getUser(id, SeaportOfficer.class).setItemCheckState(itemName, isSuccess);
    }

    @Override
    public Map<TaxInfo.Key, TaxInfo.Value> getTaxRates(String city) {
        ServerMessage.print("user get tax rates of city " + city);
        int id = GlobalQuery.getCityID(city);
        Map<TaxInfo.Key, TaxInfo.Value> map = new HashMap<>();
        for (var entry : GlobalQuery.getCityTaxRates().entrySet()) {
            if (entry.getKey().cityId() == id) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    @Override
    public Map<TaxInfo.Key, TaxInfo.Value> getTaxRates() {
        ServerMessage.print("user get all tax rates");
        return GlobalQuery.getCityTaxRates();
    }

    @Override
    public Map<String, ItemFullInfo> getCompanyItems(int id, CompanyManager.GetItemType type) {
        ServerMessage.print("company manager " + id + " get items: " + type);
        return verification.getUser(id, CompanyManager.class).getItems(type);
    }

    @Override
    public Map<Integer, StaffInfo> getCompanyCouriers(int id) throws RemoteException {
        ServerMessage.print("company manager " + id + " get couriers");
        return verification.getUser(id, CompanyManager.class).getCompanyCouriers();
    }

    @Override
    public Map<String, ShipInfo> getCompanyShips(int id, CompanyManager.GetShipType type) throws RemoteException {
        ServerMessage.print("company manager " + id + " get ships: " + type);
        return verification.getUser(id, CompanyManager.class).getShips(type);
    }

    @Override
    public Map<String, ContainerInfo> getContainers(int id, CompanyManager.GetContainerType type) throws RemoteException {
        ServerMessage.print("company manager " + id + " get containers: " + type);
        return verification.getUser(id, CompanyManager.class).getContainers(type);
    }

    @Override
    public Map<String, ItemInfo> getAllItems(int id) throws RemoteException {
        ServerMessage.print("SUSTC manager " + id + " get all items");
        return verification.getUser(id, SUSTCManager.class).getAllItems();
    }

    @Override
    public Map<Integer, StaffInfo> getAllStaffs(int id) throws RemoteException {
        ServerMessage.print("SUSTC manager " + id + " get all staffs");
        return verification.getUser(id, SUSTCManager.class).getAllStaffs();
    }

    @Override
    public Map<Integer, String> getAllCompanies() throws RemoteException {
        ServerMessage.print("user get all companies");
        return GlobalQuery.getCompanies();
    }

    @Override
    public Map<String, ShipInfo> getAllShips(int id) throws RemoteException {
        ServerMessage.print("user " + id + " get all ships");
        return verification.getUser(id, SUSTCManager.class).getAllShips();
    }

    @Override
    public Map<Integer, CityInfo> getAllCities() throws RemoteException {
        ServerMessage.print("user get all cities");
        return GlobalQuery.getCities();
    }

    @Override
    public Map<String, ContainerInfo> getAllContainers(int id) throws RemoteException {
        ServerMessage.print("user " + id + " get all containers");
        return verification.getUser(id, SUSTCManager.class).getAllContainers();
    }
}
