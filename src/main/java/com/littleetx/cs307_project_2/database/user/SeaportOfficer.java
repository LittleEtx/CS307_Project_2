package com.littleetx.cs307_project_2.database.user;

import main.interfaces.ItemState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.littleetx.cs307_project_2.database.DatabaseMapping.getStateDatabaseString;

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
        try {
            int cityId = getStaffCity();
            PreparedStatement stmt = conn.prepareStatement(
                    "select name from item_fullinfo where " +
                            "(import_city = ? and state = ? ) or " +
                            "(export_city = ? and state = ? ) ");
            stmt.setInt(1, cityId);
            stmt.setString(2, getStateDatabaseString(ItemState.ImportChecking));
            stmt.setInt(3, cityId);
            stmt.setString(4, getStateDatabaseString(ItemState.ExportChecking));
            ResultSet rs = stmt.executeQuery();
            rs = stmt.executeQuery();//找出当前城市中处于进/出口状态的所有物品
            List<String> ans = new ArrayList<>();
            while (rs.next()) {
                ans.add(rs.getString(1));
            }
            return ans.toArray(new String[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set whether the item’s checking state is success or not. Returns false
     * if item does not exist or is in illegal state
     */
    public boolean setItemCheckState(String itemName, boolean success) {
        ItemState state = getItemState(itemName);
        if (state == null) {
            return false;
        }
        if (state == ItemState.ImportChecking) {//更新物品状态
            return checkItem(itemName, success, "IMPORT", ItemState.FromImportTransporting, ItemState.ImportCheckFailed);
        } else if (state == ItemState.ExportChecking) {//更新物品状态
            return checkItem(itemName, success, "EXPORT", ItemState.PackingToContainer, ItemState.ExportCheckFailed);
        }
        return false;
    }

    private boolean checkItem(String itemName, boolean success, String stage, ItemState successState, ItemState failedState) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select city_id from item_route where stage = ? and item_name = ? ");
            stmt.setString(1, stage);
            stmt.setString(2, itemName);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next() || rs.getInt(1) != getStaffCity()) {
                return false;
            }

            stmt = conn.prepareStatement("update item_state set state= ? where item_name= ? ");
            if (success) {
                stmt.setString(1, getStateDatabaseString(successState));
            } else {
                stmt.setString(1, getStateDatabaseString(failedState));
            }
            stmt.setString(2, itemName);
            stmt.execute();

            stmt = conn.prepareStatement("insert into staff_handle_item values(?,?,?)");
            stmt.setString(1, itemName);
            stmt.setInt(2, this.id);
            stmt.setString(3, stage);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

