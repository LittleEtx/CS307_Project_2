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
        var user = verification.getUser(id, Courier.class);
        return user != null && user.newItem(name, type, price, delivery_city, export_city, import_city);
    }

    @Override
    public boolean takeItem(int id, String itemName) throws RemoteException {
        ServerMessage.print("courier " + id + " took item: " + itemName);
        var user = verification.getUser(id, Courier.class);
        return user != null && user.setItemState(itemName, ItemState.FromImportTransporting);
    }

    @Override
    public Set<String> getItemTypes() throws RemoteException {
        ServerMessage.print("user get item types");
        return GlobalQuery.getItemTypes();
    }

    @Override
    public boolean updateItemState(int id, String itemName) throws RemoteException {
        ServerMessage.print("courier " + id + " updated item: " + itemName);
        var user = verification.getUser(id, Courier.class);
        return user != null && user.updateItemState(itemName);
    }

    @Override
    public Map<String, ItemInfo> getCourierItems(int id, Courier.GetItemType type) throws RemoteException {
        ServerMessage.print("courier " + id + " get items: " + type);
        var user = verification.getUser(id, Courier.class);
        return user != null ? user.getAllItems(type) : null;
    }

    @Override
    public Map<String, ItemInfo> getItemsInPort(int id, SeaportOfficer.GetItemType type) throws RemoteException {
        ServerMessage.print("seaport officer " + id + " get items: " + type);
        var user = verification.getUser(id, SeaportOfficer.class);
        return user != null ? user.getAllItemsAtPort(type) : null;
    }

    @Override
    public boolean checkItem(int id, String itemName, boolean isSuccess) throws RemoteException {
        ServerMessage.print("seaport officer " + id + " check item " + itemName
                + " to " + (isSuccess ? "pass" : "fail"));
        var user = verification.getUser(id, SeaportOfficer.class);
        return user != null && user.setItemCheckState(itemName, isSuccess);
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
        var user = verification.getUser(id, CompanyManager.class);
        return user != null ? user.getItems(type) : null;
    }

    @Override
    public Map<Integer, StaffInfo> getCompanyCouriers(int id) throws RemoteException {
        ServerMessage.print("company manager " + id + " get couriers");
        var user = verification.getUser(id, CompanyManager.class);
        return user != null ? user.getCompanyCouriers() : null;
    }

    @Override
    public Map<String, ShipInfo> getCompanyShips(int id, CompanyManager.GetShipType type) throws RemoteException {
        ServerMessage.print("company manager " + id + " get ships: " + type);
        var user = verification.getUser(id, CompanyManager.class);
        return user != null ? user.getShips(type) : null;
    }

    @Override
    public boolean loadItemToContainer(int id, String itemName, String containerCode) throws RemoteException {
        ServerMessage.print("company manager load item " + itemName + " to container " + containerCode);
        var user = verification.getUser(id, CompanyManager.class);
        return user != null && user.loadItemToContainer(itemName, containerCode);
    }

    @Override
    public boolean loadContainerToShip(int id, String shipName, String containerCode) throws RemoteException {
        ServerMessage.print("company manager load container " + containerCode + " to ship " + shipName);
        var user = verification.getUser(id, CompanyManager.class);
        return user != null && user.loadContainerToShip(shipName, containerCode);
    }

    @Override
    public boolean shipStartSailing(int id, String shipName) throws RemoteException {
        ServerMessage.print("company manager start sailing ship " + shipName);
        var user = verification.getUser(id, CompanyManager.class);
        return user != null && user.shipStartSailing(shipName);
    }

    @Override
    public boolean unloadItem(int id, String itemName) throws RemoteException {
        ServerMessage.print("company manager unload item " + itemName);
        var user = verification.getUser(id, CompanyManager.class);
        return user != null && user.unloadItem(itemName);
    }

    @Override
    public boolean itemWaitForChecking(int id, String itemName) throws RemoteException {
        ServerMessage.print("company manager item " + itemName + " wait for checking");
        var user = verification.getUser(id, CompanyManager.class);
        return user != null && user.itemWaitForChecking(itemName);
    }

    @Override
    public Map<String, ContainerInfo> getContainers(int id, CompanyManager.GetContainerType type) throws RemoteException {
        ServerMessage.print("company manager " + id + " get containers: " + type);
        var user = verification.getUser(id, CompanyManager.class);
        return user != null ? user.getContainers(type) : null;
    }

    @Override
    public Map<String, ItemFullInfo> getAllItems(int id) throws RemoteException {
        ServerMessage.print("SUSTC manager " + id + " get all items");
        var user = verification.getUser(id, SUSTCManager.class);
        return user != null ? user.getAllItems() : null;
    }

    @Override
    public Map<Integer, StaffInfo> getAllStaffs(int id) throws RemoteException {
        ServerMessage.print("SUSTC manager " + id + " get all staffs");
        var user = verification.getUser(id, SUSTCManager.class);
        return user != null ? user.getAllStaffs() : null;
    }

    @Override
    public Map<Integer, String> getAllCompanies() throws RemoteException {
        ServerMessage.print("user get all companies");
        return GlobalQuery.getCompanies();
    }

    @Override
    public Map<String, ShipInfo> getAllShips(int id) throws RemoteException {
        ServerMessage.print("user " + id + " get all ships");
        var user = verification.getUser(id, SUSTCManager.class);
        return user != null ? user.getAllShips() : null;
    }

    @Override
    public Map<Integer, CityInfo> getAllCities() throws RemoteException {
        ServerMessage.print("user get all cities");
        return GlobalQuery.getCities();
    }

    @Override
    public Map<String, ContainerInfo> getAllContainers(int id) throws RemoteException {
        ServerMessage.print("user " + id + " get all containers");
        var user = verification.getUser(id, SUSTCManager.class);
        return user != null ? user.getAllContainers() : null;
    }

    @Override
    public boolean changePassword(int id, String newPassword) throws RemoteException {
        ServerMessage.print("user " + id + " change password to " + newPassword);
        var user = verification.getUser(id);
        return user != null && user.changePassword(newPassword);
    }

    @Override
    public boolean changePhoneNumber(int id, String newPhoneNumber) throws RemoteException {
        ServerMessage.print("user " + id + " change phone number to " + newPhoneNumber);
        var user = verification.getUser(id);
        return user != null && user.changePhoneNumber(newPhoneNumber);
    }
}
