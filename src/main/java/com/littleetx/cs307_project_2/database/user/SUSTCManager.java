package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.GlobalQuery;
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
        Map<String, ItemInfo> ans = getItems(String.format("where name= %s ", itemName));
        if (ans.size() == 0) {
            return null;
        } else {
            return ans.get(itemName);
        }
    }

    /**
     * Look for the ship’s full information according to its name. If name does not
     * exist, returns null.
     */
    public ShipInfo getShipInfo(String shipName) {
        try{
            PreparedStatement stmt= conn.prepareStatement("select state from ship_state where ship_name= ? ");//查询船状态
            stmt.setString(1,shipName);
            ResultSet rs=stmt.executeQuery();
            if (rs.next()){
                boolean isSailing= rs.getString(1).equals(DatabaseMapping.getShipState(true));
                stmt=conn.prepareStatement("select a.name,b.name from ship a join company b on a.company_id=b.id where a.name= ? ");//查询船名字和公司名字
                stmt.setString(1,shipName);
                rs=stmt.executeQuery();
                if (rs.next()){
                    return new ShipInfo(rs.getString(1),rs.getString(2),isSailing);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Look for the container’s full information according to its code. If code
     * does not exist, returns null.
     */
    public ContainerInfo getContainerInfo(String code) {
        try{
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from item_container a join item_state b on a.item_name=b.item_name"
                            + " and a.container_code= ? and b.state in (?,?,?) ");//查询该集装箱是否处于空闲状态
            stmt.setString(1, code);
            stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.PackingToContainer));
            stmt.setString(3, DatabaseMapping.getStateDatabaseString(ItemState.Shipping));
            stmt.setString(4, DatabaseMapping.getStateDatabaseString(ItemState.WaitingForShipping));
            ResultSet rs = stmt.executeQuery();
            boolean isUsing=rs.next();//查得到则为true，否则false

            stmt=conn.prepareStatement("select * from container where code= ? ");
            stmt.setString(1,code);
            rs=stmt.executeQuery();
            if (rs.next()){
            return new ContainerInfo(DatabaseMapping.getContainerInfoType(rs.getString(2)),rs.getString(1),isUsing);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Look for the staff’s full information according to his/her name. If name
     * does not exist, returns null.
     */


    public StaffInfo getStaffInfo(String staffName) {

        try {
            PreparedStatement stmt = conn.prepareStatement("select id from staff where name= ? ");//查询员工id
            stmt.setString(1,staffName);
            ResultSet rs=stmt.executeQuery();
            if (rs.next()){
                int id=rs.getInt(1);
                return GlobalQuery.getStaffInfo(conn,id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
