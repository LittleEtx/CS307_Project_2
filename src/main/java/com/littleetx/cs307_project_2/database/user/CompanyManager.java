package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.GlobalQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyManager extends User {
    CompanyManager(Connection conn, Integer id) {
        super(conn, id);
    }

    public enum TaxType {
        Import, Export
    }

    public double getTaxRate(String cityName, String itemClass, TaxType type) {
        return getTaxRate(GlobalQuery.getCompanyID(cityName), itemClass, type);
    }

    /**
     * Look for the import/export tax rate of given city and item class. Return -1
     * if any of the two names does not exist or city is not seaport city.
     */
    public double getTaxRate(int cityID, String itemClass, TaxType taxType) {
        try {
            String type;
            if (taxType == TaxType.Import) {
                type = "export_tax";
            } else {
                type = "import_tax";
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT " + type + " FROM tax_info WHERE city_id = ? AND item_type = ?");
            stmt.setInt(1, cityID);
            stmt.setString(2, itemClass);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /**
     * Notify to the database that the given item is going to be packed into the
     * given container. Returns false if the item cannot be loaded to the
     * container (due to reasons like already loaded and illegal item state) or
     * container is full or being shipped. For simplicity, one container can only
     * pack one item. Note that only item that passed export checking (at “Packing
     * to Container” state) can be loaded to container. Note that this method
     * won’t change the item’s state.
     */
    public boolean loadItemToContainer(String itemName, String containerCode) {
        //TODO
        return false;
    }

    /**
     * Notify to the database that the given container is going to be loaded to
     * the given ship. Returns false if the container cannot be loaded to the
     * ship (due to reasons like already loaded) or ship is currently sailing. For
     * simplicity, one ship can transport unlimited number of containers.
     */
    public boolean loadContainerToShip(String shipName, String containerCode) {
        //TODO
        return false;
    }

    /**
     * Notify to the database that the given ship is currently sailing with loaded
     * containers. Returns false if the ship is already sailing.
     */
    public boolean shipStartSailing(String shipName) {
        //TODO
        return false;
    }

    /**
     * Notify to the database that the given item is being unloaded from its
     * container and ship. This API also implies that the corresponding container
     * is being unloaded.
     * @return false if the item is in illegal state.
     */
    public boolean unloadItem(String itemName) {
        //TODO
        return false;
    }

    /**
     * Notify to the database that the given item is waiting at its “Import City”
     * for import checking. Returns false if the item is in illegal state.
     */
    public boolean itemWaitForChecking(String itemName) {
        //TODO
        return false;
    }
}
