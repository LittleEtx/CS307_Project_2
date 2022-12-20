package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import main.interfaces.ItemInfo;
import main.interfaces.ItemState;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

abstract public class User {
    /**
     * connection to the database
     */
    protected Connection conn;
    /**
     * id of the current staff
     */
    protected int id;

    protected User(Connection conn, int id) {
        this.id = id;
        this.conn = conn;
    }

    public boolean changePassword(String newPassword) {
        //TODO: (optional) change password
        return false;
    }

    public boolean changePhoneNumber(String newPhoneNumber) {
        //TODO: (optional) change phone number
        return false;
    }

    protected Map<String, ItemInfo> getItemsMapping(ResultSet rs) {
        Map<String, ItemInfo> items = new HashMap<>();
        try {
            while (rs.next()) {
                items.put(rs.getString("name"), new ItemInfo(
                        rs.getString("name"),
                        rs.getString("class"),
                        rs.getDouble("price"),
                        DatabaseMapping.getItemState(rs.getString("state")),
                        new ItemInfo.RetrievalDeliveryInfo(
                                GlobalQuery.getCityName(rs.getInt("retrieval_city")),
                                rs.getString("retrieval_staff")
                        ),
                        new ItemInfo.RetrievalDeliveryInfo(
                                GlobalQuery.getCityName(rs.getInt("delivery_city")),
                                rs.getString("delivery_staff")
                        ),
                        new ItemInfo.ImportExportInfo(
                                GlobalQuery.getCityName(rs.getInt("import_city")),
                                rs.getString("import_staff"),
                                rs.getDouble("price") * GlobalQuery.getCityTaxRate(rs.getInt("import_city"),
                                        rs.getString("class")).import_rate
                        ),
                        new ItemInfo.ImportExportInfo(
                                GlobalQuery.getCityName(rs.getInt("export_city")),
                                rs.getString("export_staff"),
                                rs.getDouble("price") * GlobalQuery.getCityTaxRate(rs.getInt("export_city"),
                                        rs.getString("class")).export_rate
                        )
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    protected ItemState getItemState(String itemName) {
        try {
            var stmt = conn.prepareStatement(
                    "select state from item_state where item_name = ?"
            );
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return DatabaseMapping.getItemState(rs.getString(1));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected int getItemCompany(String itemName) {
        try {
            var stmt = conn.prepareStatement("select company_id from item_company where item_name = ?");
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    protected int getStaffCompany() {
        try {
            var stmt = conn.prepareStatement("select company_id from staff_company where staff_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    protected int getStaffCity() {
        try {
            var stmt = conn.prepareStatement("select city_id from staff_city where staff_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    protected String getStaffName() {
        try {
            var stmt = conn.prepareStatement("select name from staff where id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
