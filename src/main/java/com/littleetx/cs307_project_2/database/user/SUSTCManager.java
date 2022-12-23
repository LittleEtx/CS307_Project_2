package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.ViewMapping;
import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import main.interfaces.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SUSTCManager extends User {
    public SUSTCManager(Connection conn, Integer id) {
        super(conn, id);
    }

    public enum CountType {
        City, Courier, Ship, Company
    }

    /**
     * Look for the number of companies/cities/couriers/ships that logged SUSTC
     */
    public int getCount(CountType type) {
        try {
            ResultSet rs;
            PreparedStatement stmt;
            if (type.equals(CountType.City)) {
                stmt = conn.prepareStatement("select count(*) from city");
                rs = stmt.executeQuery();
                if (rs.next())
                    return rs.getInt(1);
            } else if (type.equals(CountType.Courier)) {
                stmt = conn.prepareStatement("select count(*) from staff a join verification b on a.id = b.staff_id " +
                        "where b.authority= ? ");
                stmt.setString(1, DatabaseMapping.getStaffAuthorityDatabaseStr(LogInfo.StaffType.Courier));
                rs = stmt.executeQuery();
                if (rs.next())
                    return rs.getInt(1);
            } else if (type.equals(CountType.Ship)) {
                stmt = conn.prepareStatement("select count(*) from ship");
                rs = stmt.executeQuery();
                if (rs.next())
                    return rs.getInt(1);
            } else if (type.equals(CountType.Company)) {
                stmt = conn.prepareStatement("select count(*) from company");
                rs = stmt.executeQuery();
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /**
     * Look for the item’s full information according to its name. If name does
     * not exist, returns null.
     */
    public ItemInfo getItemInfo(String itemName) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from item_fullinfo where name = ?");
            stmt.setString(1, itemName);
            Map<String, ItemInfo> ans = ViewMapping.getItemsMapping(stmt.executeQuery());
            if (ans.size() == 0) {
                return null;
            } else {
                return ans.get(itemName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Look for the ship’s full information according to its name. If name does not
     * exist, returns null.
     */
    public ShipInfo getShipInfo(String shipName) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from ship_info where name = ?");
            stmt.setString(1, shipName);
            var result = ViewMapping.getShipsMapping(stmt.executeQuery());
            return result.get(shipName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Look for the container’s full information according to its code. If code
     * does not exist, returns null.
     */
    public ContainerInfo getContainerInfo(String code) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from container_info where code = ?");//查询该集装箱是否处于空闲状态
            stmt.setString(1, code);
            var result = ViewMapping.getContainersMapping(stmt.executeQuery());
            return result.get(code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Look for the staff’s full information according to his/her name. If name
     * does not exist, returns null.
     */


    public StaffInfo getStaffInfo(String staffName) {

        try {
            PreparedStatement stmt = conn.prepareStatement("select * from staff_info where name= ? ");//查询员工id
            stmt.setString(1, staffName);
            var result = ViewMapping.getStaffsMapping(stmt.executeQuery(), true);
            return result.isEmpty() ? null : result.values().iterator().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ItemFullInfo> getAllItems() {
        try {
            PreparedStatement stmt = conn.prepareStatement("select * from item_info_extra");
            return ViewMapping.getItemFullInfoMapping(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, StaffInfo> getAllStaffs() {
        try {
            PreparedStatement stmt = conn.prepareStatement("select * from staff_info");
            return ViewMapping.getStaffsMapping(stmt.executeQuery(), false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        return super.getAllContainers();
    }

}
