package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.ViewMapping;
import main.interfaces.ContainerInfo;
import main.interfaces.ItemState;
import main.interfaces.ShipInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Map<String, ShipInfo> getAllShips() {
        try {
            var stmt = conn.prepareStatement("select * from ship_info");
            return ViewMapping.getShipsMapping(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ContainerInfo> getAllContainers() {
        try {
            var stmt = conn.prepareStatement("select * from container_info");
            return ViewMapping.getContainersMapping(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
