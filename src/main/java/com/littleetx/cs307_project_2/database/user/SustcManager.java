package com.littleetx.cs307_project_2.database.user;

import cs307.project2.interfaces.*;

import java.sql.Connection;

public class SustcManager extends User {
    public SustcManager(Connection conn, Integer id, StaffInfo info) {
        super(conn, id, info);
    }

    @Override
    public LogInfo.StaffType getStaffType() {
        return LogInfo.StaffType.SustcManager;
    }

    public enum CountType {
        City, Courier, Ship, Company
    }
    /**
     * Look for the number of companies/cities/couriers/ships that logged SUSTC
     */
    public int getCount(CountType type) {
        //TODO
        return 0;
    }

    /**
     * Look for the item’s full information according to its name. If name does
     * not exist, returns null.
     */
    public ItemInfo getItemInfo(String itemName) {
        //TODO
        return null;
    }

    /**
     * Look for the ship’s full information according to its name. If name does not
     * exist, returns null.
     */
    public ShipInfo getShipInfo(String shipName) {
        //TODO
        return null;
    }

    /**
     * Look for the container’s full information according to its code. If code
     * does not exist, returns null.
     */
    public ContainerInfo getContainerInfo(String code) {
        //TODO
        return null;
    }

    /**
     * Look for the staff’s full information according to his/her name. If name
     * does not exist, returns null.
     */
    public StaffInfo getStaffInfo(String staffName) {
        //TODO
        return null;
    }
}
