package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import com.littleetx.cs307_project_2.database.ViewMapping;
import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import main.interfaces.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static com.littleetx.cs307_project_2.database.DatabaseMapping.getIsSailing;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.getStateDatabaseString;

public class CompanyManager extends User {
    public CompanyManager(Connection conn, Integer id) {
        super(conn, id);
    }

    public enum TaxType {
        Import, Export
    }

    public double getTaxRate(String cityName, String itemClass, TaxType type) {
        return getTaxRate(GlobalQuery.getCityID(cityName), itemClass, type);
    }

    /**
     * Look for the import/export tax rate of given city and item class. Return -1
     * if any of the two names does not exist or city is not seaport city.
     */
    public double getTaxRate(int cityID, String itemClass, TaxType taxType) {
        try {
            String type;
            if (taxType == TaxType.Import) {
                type = "import_rate";
            } else {
                type = "export_rate";
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
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from item_container a join item_state b on a.item_name = b.item_name"
                            + " and a.container_code= ? and b.state in (?,?,?) ");//查询是否该集装箱处于有物品装载的状态
            stmt.setString(1, containerCode);
            stmt.setString(2, getStateDatabaseString(ItemState.PackingToContainer));
            stmt.setString(3, getStateDatabaseString(ItemState.WaitingForShipping));
            stmt.setString(4, getStateDatabaseString(ItemState.Shipping));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }
            //说明集装箱是空闲的
            if (getItemState(itemName) == ItemState.PackingToContainer) {//说明物品状态是正确的
                stmt = conn.prepareStatement("select * from item_container where item_name = ?");
                stmt.setString(1, itemName);
                rs = stmt.executeQuery();

                if (!rs.next()) {//如果该物品目前未被装入别的集装箱
                    stmt = conn.prepareStatement("insert into item_container values(?,?)");
                    stmt.setString(1, itemName);
                    stmt.setString(2, containerCode);
                } else {//如果该物品目前正装在别的集装箱
                    stmt = conn.prepareStatement("update item_container set container_code = ? where item_name = ?");
                    stmt.setString(1, containerCode);
                    stmt.setString(2, itemName);
                }
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Notify to the database that the given container is going to be loaded to
     * the given ship. Returns false if the container cannot be loaded to the
     * ship (due to reasons like already loaded) or ship is currently sailing. For
     * simplicity, one ship can transport unlimited number of containers.
     */
    public boolean loadContainerToShip(String shipName, String containerCode) {////
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select a.item_name from item_container a join item_state b on a.item_name = b.item_name "
                            + "where a.container_code= ? and b.state= ? "
            );//查询是否该集装箱处于正确状态
            stmt.setString(1, containerCode);
            stmt.setString(2, getStateDatabaseString(ItemState.PackingToContainer));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {//true代表container状态合法
                String item_name = rs.getString(1);//得到该集装箱装的物品

                //check if the ship belongs to the company
                int company_id = getShipCompany(shipName);
                if (company_id != getItemCompany(item_name) || company_id != getStaffCompany()) {
                    return false;
                }

                //check ship state
                stmt = conn.prepareStatement("select * from ship_info where name = ?");
                stmt.setString(1, shipName);
                rs = stmt.executeQuery();
                if (!rs.next() || getIsSailing(rs.getString("state"))) {
                    return false;
                }

                //load item to ship
                stmt = conn.prepareStatement("insert into item_ship values(?,?)");
                stmt.setString(1, item_name);
                stmt.setString(2, shipName);
                stmt.execute();//插入进item_ship的记录

                stmt = conn.prepareStatement("update item_state set state = ?  where item_name = ?");
                stmt.setString(1, getStateDatabaseString(ItemState.WaitingForShipping));
                stmt.setString(2, item_name);//更新item状态
                stmt.execute();
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private int getShipCompany(String shipName) throws SQLException {
        PreparedStatement stmt;
        stmt = conn.prepareStatement("select company_id from ship where name = ?");
        stmt.setString(1, shipName);
        var result = stmt.executeQuery();
        return result.next() ? result.getInt(1) : -1;
    }

    /**
     * Notify to the database that the given ship is currently sailing with loaded
     * containers. Returns false if the ship is already sailing.
     */
    public boolean shipStartSailing(String shipName) {
        try {
            if (getShipCompany(shipName) != getStaffCompany()) {//判断船公司与用户公司是否相同
                return false;
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from ship_info where name= ? ");
            stmt.setString(1, shipName);
            ResultSet rs = stmt.executeQuery();//查询是否是否船处于docked状态
            if (rs.next() && !getIsSailing(rs.getString("state"))) {//如果是docked才能继续
                stmt = conn.prepareStatement("select a.item_name from item_ship a join item_state b on a.item_name=b.item_name " +
                        " where a.ship_name= ? and b.state= ? ");
                stmt.setString(1, shipName);
                stmt.setString(2, getStateDatabaseString(ItemState.WaitingForShipping));
                rs = stmt.executeQuery();//查询当前船上的所有item_name
                int count = 0;
                while (rs.next()) {
                    count++;
                    stmt = conn.prepareStatement("update item_state set state= ? where item_name= ?");
                    stmt.setString(1, getStateDatabaseString(ItemState.Shipping));
                    stmt.setString(2, rs.getString(1));//更新当前船上所有item状态
                    stmt.execute();
                }
                if (count != 0) {//如果不是空船
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Notify to the database that the given item is being unloaded from its
     * container and ship. This API also implies that the corresponding container
     * is being unloaded.
     *
     * @return false if the item is in illegal state.
     */
    public boolean unloadItem(String itemName) {
        try {
            //确认物品公司与用户公司相同
            if (getItemCompany(itemName) != getStaffCompany()) {
                return false;
            }
            ItemState state = getItemState(itemName);
            if (state == ItemState.Shipping) {//确认该物品状态合法
                PreparedStatement stmt = conn.prepareStatement("update item_state set state= ? where item_name = ? ");//更新物品状态
                stmt.setString(1, getStateDatabaseString(ItemState.UnpackingFromContainer));
                stmt.setString(2, itemName);
                stmt.execute();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Notify to the database that the given item is waiting at its “Import City”
     * for import checking. Returns false if the item is in illegal state.
     */
    public boolean itemWaitForChecking(String itemName) {
        try {
            if (getItemCompany(itemName) == getStaffCompany()) {//如果物品公司与用户公司相同
                PreparedStatement stmt = conn.prepareStatement("select state from item_state where item_name = ? ");//查询该物品状态
                stmt.setString(1, itemName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() &&
                        rs.getString(1).equals(getStateDatabaseString(ItemState.UnpackingFromContainer))) {//如果存在该物品且状态合法
                    stmt = conn.prepareStatement("update item_state set state= ? where item_name = ? ");
                    stmt.setString(1, getStateDatabaseString(ItemState.ImportChecking));//更新物品状态
                    stmt.setString(2, itemName);
                    stmt.execute();
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public enum GetItemType {
        IMPORT, ON_SHIP, EXPORT, ALL
    }

    public Map<String, ItemFullInfo> getItems(GetItemType type) {
        try {
            PreparedStatement stmt;
            switch (type) {
                case IMPORT -> {
                    stmt = conn.prepareStatement(
                            "select * from item_info_extra where company = ? and state = ? ");
                    stmt.setInt(1, getStaffCompany());
                    stmt.setString(2, getStateDatabaseString(ItemState.UnpackingFromContainer));
                }
                case ON_SHIP -> {
                    stmt = conn.prepareStatement(
                            "select * from item_info_extra where company = ? and state in (?, ?) ");
                    stmt.setInt(1, getStaffCompany());
                    stmt.setString(2, getStateDatabaseString(ItemState.WaitingForShipping));
                    stmt.setString(3, getStateDatabaseString(ItemState.Shipping));
                }
                case EXPORT -> {
                    stmt = conn.prepareStatement(
                            "select * from item_info_extra where company = ? and state = ? ");
                    stmt.setInt(1, getStaffCompany());
                    stmt.setString(2, getStateDatabaseString(ItemState.PackingToContainer));
                }
                case ALL -> {
                    stmt = conn.prepareStatement(
                            "select * from item_info_extra where company = ? ");
                    stmt.setInt(1, getStaffCompany());
                }
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }
            return ViewMapping.getItemFullInfoMapping(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public enum GetShipType {
        DOCKED, ALL
    }

    public Map<String, ShipInfo> getShips(GetShipType type) {
        try {
            PreparedStatement stmt;
            switch (type) {
                case DOCKED -> {
                    stmt = conn.prepareStatement(
                            "select * from ship_info where company = ? and state = ? ");
                    stmt.setInt(1, getStaffCompany());
                    stmt.setString(2, DatabaseMapping.getIsSailing(false));
                }
                case ALL -> {
                    stmt = conn.prepareStatement(
                            "select * from ship_info where company = ? ");
                    stmt.setInt(1, getStaffCompany());
                }
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }
            return ViewMapping.getShipsMapping(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public enum GetContainerType {
        IDLE, ALL
    }

    public Map<String, ContainerInfo> getContainers(GetContainerType type) {
        switch (type) {
            case IDLE -> {
                try {
                    PreparedStatement stmt = conn.prepareStatement(
                            "select * from container_info where state = ? ");
                    stmt.setString(1, DatabaseMapping.getIsContainerUsing(false));
                    return ViewMapping.getContainersMapping(stmt.executeQuery());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            case ALL -> {
                return super.getAllContainers();
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public Map<Integer, StaffInfo> getCompanyCouriers() {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from staff_info where company = ? and authority = ? ");
            stmt.setInt(1, getStaffCompany());
            stmt.setString(2, DatabaseMapping
                    .getStaffAuthorityDatabaseStr(LogInfo.StaffType.Courier));
            return ViewMapping.getStaffsMapping(stmt.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
