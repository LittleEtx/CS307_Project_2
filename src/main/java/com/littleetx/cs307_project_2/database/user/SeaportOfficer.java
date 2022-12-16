package com.littleetx.cs307_project_2.database.user;

import java.sql.Connection;

public class SeaportOfficer extends User {

    public SeaportOfficer(Connection conn, Integer id) {
        super(conn, id);
    }

    /**
     * Look for all the items that is currently waiting at this officer’s working
     * seaport.
     *
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
