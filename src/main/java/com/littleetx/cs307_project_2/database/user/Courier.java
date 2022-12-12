package com.littleetx.cs307_project_2.database.user;

import cs307.project2.interfaces.ItemInfo;
import cs307.project2.interfaces.ItemState;
import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.sql.Connection;

public class Courier extends User {
    public Courier(Connection conn, StaffInfo info) {
        super(conn, info);
    }

    @Override
    public  LogInfo.StaffType getStaffType() {
        return LogInfo.StaffType.Courier;
    }

    /**
     * Add a new item that is currently being picked up. The item shall contain
     * all necessary information(basic info and four cities). Returns false if item
     * already exist or contains illegal information
     */
    public boolean newItem(ItemInfo item) {
        //TODO
        return false;
    }

    /**
     * Set the item’s current state to the given state with given item name.
     * @return false if item already exist or s is illegal
     */
    public boolean setItemState(String itemName, ItemState itemState) {
        //TODO
        return false;
    }
}
