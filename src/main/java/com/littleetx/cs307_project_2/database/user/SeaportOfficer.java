package com.littleetx.cs307_project_2.database.user;

import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.sql.Connection;

public class SeaportOfficer extends User {

    public SeaportOfficer(Connection conn, StaffInfo info) throws StaffTypeNotMatchException {
        super(conn, info);
    }

    @Override
    public LogInfo.StaffType getStaffType() {
        return LogInfo.StaffType.SeaportOfficer;
    }

    /**
     * Look for all the items that is currently waiting at this officer’s working
     * seaport.
     * @return an array of String containing the item names.
     */
    public String[] getAllItemsAtPort() {
        //TODO
        return null;
    }

    /**
     * Set whether the item’s checking state is success or not. Returns false
     * if item does not exist or is in illegal state
     */
    public boolean setItemCheckState(String itemName, boolean success) {
        //TODO
        return false;
    }
}
