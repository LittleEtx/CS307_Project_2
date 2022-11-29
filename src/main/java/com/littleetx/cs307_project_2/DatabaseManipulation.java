package com.littleetx.cs307_project_2;

import com.littleetx.cs307_project_2.user.*;
import cs307.project2.interfaces.*;

import java.sql.Connection;

public class DatabaseManipulation implements IDatabaseManipulation {
    private final Connection rootConn;
    private final Verification<CompanyManager> companyManagers;
    private final Verification<SeaportOfficer> seaportOfficers;
    private final Verification<Courier> couriers;
    private final Verification<SustcManager> sustcManagers;


    DatabaseManipulation(String database, String root, String pass) {
        //TODO: connect to database
        rootConn = null;

        //TODO: create tables

        //TODO: create new users and create verification factory
        companyManagers = null;
        seaportOfficers = null;
        couriers = null;
        sustcManagers = null;
    }

    @Override
    public void $import(String recordsCSV, String staffsCSV) {
        //TODO
    }

    @Override
    public double getImportTaxRate(LogInfo log, String city, String itemClass) {
        var user = companyManagers.getUser(log);
        return user != null ? user.getTaxRate(city, itemClass, CompanyManager.TaxType.Import) : -1;
    }

    @Override
    public double getExportTaxRate(LogInfo log, String city, String itemClass) {
        var user = companyManagers.getUser(log);
        return user != null ? user.getTaxRate(city, itemClass, CompanyManager.TaxType.Export) : -1;
    }

    @Override
    public boolean loadItemToContainer(LogInfo log, String itemName, String containerCode) {
        var user = companyManagers.getUser(log);
        return user != null && user.loadItemToContainer(itemName, containerCode);
    }

    @Override
    public boolean loadContainerToShip(LogInfo log, String shipName, String containerCode) {
        var user = companyManagers.getUser(log);
        return user != null && user.loadContainerToShip(shipName, containerCode);
    }

    @Override
    public boolean shipStartSailing(LogInfo log, String shipName) {
        var user = companyManagers.getUser(log);
        return user != null && user.shipStartSailing(shipName);
    }

    @Override
    public boolean unloadItem(LogInfo log, String itemName) {
        var user = companyManagers.getUser(log);
        return user != null && user.unloadItem(itemName);
    }

    @Override
    public boolean itemWaitForChecking(LogInfo log, String item) {
        var user = companyManagers.getUser(log);
        return user != null && user.itemWaitForChecking(item);
    }

    @Override
    public boolean newItem(LogInfo log, ItemInfo item) {
        var user = couriers.getUser(log);
        return user != null && user.newItem(item);
    }

    @Override
    public boolean setItemState(LogInfo log, String itemName, ItemState state) {
        var user = couriers.getUser(log);
        return user != null && user.setItemState(itemName, state);
    }

    @Override
    public String[] getAllItemsAtPort(LogInfo log) {
        var user = seaportOfficers.getUser(log);
        return user != null ? user.getAllItemsAtPort() : null;
    }

    @Override
    public boolean setItemCheckState(LogInfo log, String itemName, boolean success) {
        var user = seaportOfficers.getUser(log);
        return user != null && user.setItemCheckState(itemName, success);
    }

    @Override
    public int getCompanyCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.Company) : -1;
    }

    @Override
    public int getCityCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.City) : -1;
    }

    @Override
    public int getCourierCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.Courier) : -1;
    }

    @Override
    public int getShipCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.Ship) : -1;
    }

    @Override
    public ItemInfo getItemInfo(LogInfo log, String name) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getItemInfo(name) : null;
    }

    @Override
    public ShipInfo getShipInfo(LogInfo log, String name) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getShipInfo(name) : null;
    }

    @Override
    public ContainerInfo getContainerInfo(LogInfo log, String code) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getContainerInfo(code) : null;
    }

    @Override
    public StaffInfo getStaffInfo(LogInfo log, String name) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getStaffInfo(name) : null;
    }
}
