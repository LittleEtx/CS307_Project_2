package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import main.interfaces.ItemState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                type = "export_rate";
            } else {
                type = "import_rate";
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
        String code;
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from item_container a join item_state b on a.item_name=b.item_name"
                            + " and a.container_code= ? and b.state in (?,?,?) ");//查询是否该集装箱处于有物品装载的状态
            stmt.setString(1, containerCode);
            stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.PackingToContainer));
            stmt.setString(3, DatabaseMapping.getStateDatabaseString(ItemState.Shipping));
            stmt.setString(4, DatabaseMapping.getStateDatabaseString(ItemState.WaitingForShipping));
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {//说明集装箱是空闲的


                if (getItemCompany(itemName) == getStaffCompany(this.id)) {//判断用户公司与物品公司是否相同

                    stmt = conn.prepareStatement("select state from item_state where item_name= ?");
                    stmt.setString(1, itemName);
                    rs = stmt.executeQuery();
                    if (rs.next() && rs.getString(1).equals("Packing to Container")) {//说明物品状态是正确的
                        stmt = conn.prepareStatement("select * from item_container where item_name = ?");
                        stmt.setString(1, itemName);
                        rs = stmt.executeQuery();

                        if (!rs.next()) {//如果该物品目前未被装入别的集装箱
                            stmt = conn.prepareStatement("insert into item_container values(?,?)");
                            stmt.setString(1, itemName);
                            stmt.setString(2, containerCode);
                            stmt.execute();
                            return true;
                        } else {//如果该物品目前正装在别的集装箱
                            stmt = conn.prepareStatement("update item_container set container_code = ? where item_name = ?");
                            stmt.setString(1, containerCode);
                            stmt.setString(2, itemName);
                            stmt.execute();
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public int getItemCompany(String itemName) {
        try {
            PreparedStatement stmt = conn.prepareStatement("select b.company_id from staff_handle_item a join staff_company b " +
                    "on a.staff_id=b.staff_id " +
                    " where a.item_name= ? ");
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

    public int getStaffCompany(int staff_id) {
        try {
            PreparedStatement stmt = conn.prepareStatement("select company_id from staff_company where staff_id = ? ");
            stmt.setInt(1, staff_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;

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
                    "select a.item_name from item_container a join item_state b on a.item_name=b.item_name "
                            + "where a.container_code= ? and b.state= ? "
            );//查询是否该集装箱处于正确状态
            stmt.setString(1, containerCode);
            stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.PackingToContainer));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {//true代表container状态合法
                String item_name = rs.getString(1);//得到该集装箱装的物品
                int itemCompany = getItemCompany(item_name);//得到该物品的公司
                stmt = conn.prepareStatement("select company_id from ship where name= ? ");
                stmt.setString(1, shipName);
                rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == itemCompany && itemCompany == getStaffCompany(this.id)) {//判断物品公司与船公司与用户公司是否相同
                    stmt = conn.prepareStatement("select * from item_ship a join item_state b where a.ship_name= ? and b.state= ?");
                    stmt.setString(1, shipName);
                    stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.Shipping));
                    rs = stmt.executeQuery();//查询是否船上有物品（是否船处于docked状态）

                    if (!rs.next()) {//如果船处于docked状态才能继续
                        stmt = conn.prepareStatement("select * from item_ship where item_name = ?");
                        stmt.setString(1, item_name);
                        rs = stmt.executeQuery();//查询是否该物品曾到过船上

                        if (!rs.next()) {//若没有，则装船，新增记录
                            stmt = conn.prepareStatement("insert into item_ship values(?,?)");
                            stmt.setString(1, item_name);
                            stmt.setString(2, shipName);
                            stmt.execute();//插入进item_ship的记录
                        } else {//若item曾到过船上，则update记录，更改为当前船（虽然理论上不应该出现这种情况）
                            stmt = conn.prepareStatement("update item_ship set ship_name= ? where item_name= ? ");
                            stmt.setString(1, shipName);
                            stmt.setString(2, item_name);
                            stmt.execute();
                        }
                        stmt = conn.prepareStatement("update item_state set state=?  where item_name= ?");
                        stmt.setString(1, DatabaseMapping.getStateDatabaseString(ItemState.WaitingForShipping));
                        stmt.setString(2, item_name);//更新item状态
                        stmt.execute();
                        return true;
                    }

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Notify to the database that the given ship is currently sailing with loaded
     * containers. Returns false if the ship is already sailing.
     */
    public boolean shipStartSailing(String shipName) {
        try {
            PreparedStatement stmt = conn.prepareStatement("select * from item_ship a join item_state b on a.item_name=b.item_name " +
                    " where a.ship_name= ? and b.state= ?");
            stmt.setString(1, shipName);
            stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.Shipping));
            ResultSet rs = stmt.executeQuery();//查询是否船上有物品（是否船处于docked状态）
            if (!rs.next()) {//如果是docked才能继续
                stmt = conn.prepareStatement("select a.item_name from item_ship a join item_state b on a.item_name=b.item_name " +
                        " where a.ship_name= ? and b.state= ? ");
                stmt.setString(1, shipName);
                stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.WaitingForShipping));
                rs = stmt.executeQuery();//查询当前船上的所有item_name
                int count = 0;
                while (rs.next()) {
                    count++;
                    stmt = conn.prepareStatement("update item_state set state= ? where item_name= ?");
                    stmt.setString(1, DatabaseMapping.getStateDatabaseString(ItemState.Shipping));
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
            PreparedStatement stmt = conn.prepareStatement("select state from item_state where item_name = ? ");
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() &&
                    rs.getString(1).equals(DatabaseMapping.getStateDatabaseString(ItemState.Shipping))) {//确认该物品状态合法
                if (getItemCompany(itemName) == getStaffCompany(this.id)) {//确认物品公司与用户公司相同
                    stmt = conn.prepareStatement("update item_state set state= ? where item_name = ? ");//更新物品状态
                    stmt.setString(1, DatabaseMapping.getStateDatabaseString(ItemState.UnpackingFromContainer));
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

    /**
     * Notify to the database that the given item is waiting at its “Import City”
     * for import checking. Returns false if the item is in illegal state.
     */
    public boolean itemWaitForChecking(String itemName) {
        try {
            if (getItemCompany(itemName) == getStaffCompany(this.id)) {//如果物品公司与用户公司相同
                PreparedStatement stmt = conn.prepareStatement("select state from item_state where item_name = ? ");//查询该物品状态
                stmt.setString(1, itemName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() &&
                        rs.getString(1).equals(DatabaseMapping.getStateDatabaseString(ItemState.UnpackingFromContainer))) {//如果存在该物品且状态合法
                    stmt = conn.prepareStatement("update item_state set state= ? where item_name = ? ");
                    stmt.setString(1, DatabaseMapping.getStateDatabaseString(ItemState.ImportChecking));//更新物品状态
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
}
