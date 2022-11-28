package com.littleetx.cs307_project_2;
import cs307.project2.interfaces.*;

public class DatabaseManipulation implements IDatabaseManipulation {
    DatabaseManipulation(String database, String root, String pass) {
    }

    @Override
    public void $import(String recordsCSV, String staffsCSV) {

    }

    @Override
    public double getImportTaxRate(LogInfo log, String city, String itemClass) {
        return 0;
    }

    @Override
    public double getExportTaxRate(LogInfo log, String city, String itemClass) {
        return 0;
    }

    @Override
    public boolean loadItemToContainer(LogInfo log, String itemName, String containerCode) {
        return false;
    }

    @Override
    public boolean loadContainerToShip(LogInfo log, String shipName, String containerCode) {
        return false;
    }

    @Override
    public boolean shipStartSailing(LogInfo log, String shipName) {
        return false;
    }

    @Override
    public boolean unloadItem(LogInfo log, String itemName) {
        return false;
    }

    @Override
    public boolean itemWaitForChecking(LogInfo log, String item) {
        return false;
    }

    @Override
    public boolean newItem(LogInfo log, ItemInfo item) {
        return false;
    }

    @Override
    public boolean setItemState(LogInfo log, String name, ItemState s) {
        return false;
    }

    @Override
    public String[] getAllItemsAtPort(LogInfo log) {
        return new String[0];
    }

    @Override
    public boolean setItemCheckState(LogInfo log, String itemName, boolean success) {
        return false;
    }

    @Override
    public int getCompanyCount(LogInfo log) {
        return 0;
    }

    @Override
    public int getCityCount(LogInfo log) {
        return 0;
    }

    @Override
    public int getCourierCount(LogInfo log) {
        return 0;
    }

    @Override
    public int getShipCount(LogInfo log) {
        return 0;
    }

    @Override
    public ItemInfo getItemInfo(LogInfo log, String name) {
        return null;
    }

    @Override
    public ShipInfo getShipInfo(LogInfo log, String name) {
        return null;
    }

    @Override
    public ContainerInfo getContainerInfo(LogInfo log, String code) {
        return null;
    }

    @Override
    public StaffInfo getStaffInfo(LogInfo log, String name) {
        return null;
    }
}
