package com.littleetx.cs307_project_2.database.user;

import main.interfaces.ItemInfo;
import main.interfaces.ItemState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static com.littleetx.cs307_project_2.database.DatabaseMapping.getStateDatabaseString;
import static com.littleetx.cs307_project_2.database.GlobalQuery.getCityID;
import static com.littleetx.cs307_project_2.database.GlobalQuery.getCityTaxRate;

public class Courier extends User {
    private static String getList(ItemState[] states) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(getStateDatabaseString(states[0]));
        sb.append("'");
        for (int i = 1; i < states.length; i++) {
            sb.append(", '");
            sb.append(getStateDatabaseString(states[i]));
            sb.append("'");
        }
        return sb.toString();
    }

    public Courier(Connection conn, Integer id) {
        super(conn, id);
    }

    /**
     * Add a new item that is currently being picked up. The item shall contain
     * all necessary information(basic info and four cities). Returns false if item
     * already exist or contains illegal information
     */
    public boolean newItem(ItemInfo item) {
        try {
            if (item.$import() == null || item.export() == null || item.retrieval() == null || item.delivery() == null)
                return false;

            PreparedStatement stmt = conn.prepareStatement("select * from item where name= ? ");//看是否能查到该item
            stmt.setString(1, item.name());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {//表中查不到该item说明item之前不存在，状态合法
                stmt = conn.prepareStatement("select city_id from staff_city where staff_id = ? ");
                stmt.setInt(1, id);
                rs = stmt.executeQuery();
                rs.next();
                int cityId = rs.getInt(1);
                if (getCityID(item.retrieval().city()) != cityId || item.name() == null || item.$class() == null//非空
                        || item.$import().city() == null || item.delivery().city() == null || item.export().city() == null
                        //city not identical
                        || item.$import().city().equals(item.export().city())
                        || item.delivery().city().equals(item.retrieval().city())) {
                    return false;
                }
                int exportCityId = getCityID(item.export().city());
                int importCityId = getCityID(item.$import().city());
                //check tax rate
                if (Math.abs(item.price() * getCityTaxRate(exportCityId, item.$class()).export_rate - item.export().tax()) > 0.01 ||
                        Math.abs(item.price() * getCityTaxRate(importCityId, item.$class()).import_rate - item.$import().tax()) > 0.01) {
                    return false;
                }

                stmt = conn.prepareStatement("insert into item values(?,?,?)");
                stmt.setString(1, item.name());
                stmt.setInt(2, (int) item.price());
                stmt.setString(3, item.$class());
                stmt.execute();

                stmt = conn.prepareStatement("insert into item_route values(?,?,?)");
                stmt.setString(1, item.name());
                stmt.setInt(2, getCityID(item.retrieval().city()));
                stmt.setString(3, "RETRIEVAL");
                stmt.addBatch();
                stmt.setString(1, item.name());
                stmt.setInt(2, getCityID(item.export().city()));
                stmt.setString(3, "EXPORT");
                stmt.addBatch();
                stmt.setString(1, item.name());
                stmt.setInt(2, getCityID(item.$import().city()));
                stmt.setString(3, "IMPORT");
                stmt.addBatch();
                stmt.setString(1, item.name());
                stmt.setInt(2, getCityID(item.delivery().city()));
                stmt.setString(3, "DELIVERY");
                stmt.executeBatch();

                stmt = conn.prepareStatement("insert into item_state values(?,?)");
                stmt.setString(1, item.name());
                stmt.setString(2, getStateDatabaseString(ItemState.PickingUp));
                stmt.execute();

                stmt = conn.prepareStatement("insert into staff_handle_item values(?,?,?)");
                stmt.setString(1, item.name());
                stmt.setInt(2, this.id);
                stmt.setString(3, "RETRIEVAL");
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Set the item’s current state to the given state with given item name.
     *
     * @return false if item already exist or s is illegal
     */
    public boolean setItemState(String itemName, ItemState newState) {
        try {
            ItemState itemState = getItemState(itemName);
            if (itemState == null) {
                return false;
            }
            //get new item
            if (newState == ItemState.FromImportTransporting &&
                    itemState == ItemState.FromImportTransporting) {
                PreparedStatement stmt = conn.prepareStatement(
                        "select stage from staff_handle_item where item_name= ? and stage= ? ");
                stmt.setString(1, itemName);
                stmt.setString(2, "DELIVERY");

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {//如果已被人负责，状态非法
                    return false;
                }
                stmt = conn.prepareStatement(
                        "select city_id from item_route where item_name= ? and stage = 'DELIVERY'");
                stmt.setString(1, itemName);
                rs = stmt.executeQuery();//查询物品所在城市
                rs.next();
                int itemCity = rs.getInt(1);
                if (itemCity == getStaffCity()) {//城市相同则合法
                    stmt = conn.prepareStatement("insert into staff_handle_item values(?,?,?)");//新增员工负责物品信息
                    stmt.setString(1, itemName);
                    stmt.setInt(2, this.id);
                    stmt.setString(3, "DELIVERY");
                    stmt.execute();
                    return true;//此情况无需更新物品状态，维持原状态
                } else {
                    return false;
                }
            }

            //get to the next stage
            PreparedStatement stmt = conn.prepareStatement(
                    "select * from staff_handle_item where item_name= ? and staff_id= ? and stage= ? ");
            stmt.setString(1, itemName);
            stmt.setInt(2, this.id);
            if ((newState == ItemState.ToExportTransporting && itemState == ItemState.PickingUp) ||
                    (newState == ItemState.ExportChecking && itemState == ItemState.ToExportTransporting)) {
                stmt.setString(3, "RETRIEVAL");
            } else if ((newState == ItemState.Delivering && itemState == ItemState.FromImportTransporting) ||
                    (newState == ItemState.Finish && itemState == ItemState.Delivering)) {
                stmt.setString(3, "DELIVERY");
            } else {
                return false;
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {//如果item状态合法，且当前用户确实负责当前物品
                stmt = conn.prepareStatement("update item_state set state= ? where item_name= ? ");//更新物品状态
                stmt.setString(1, getStateDatabaseString(newState));
                stmt.setString(2, itemName);
                stmt.execute();
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public enum GetItemType {
        New, OnGoing, Finished
    }

    public Map<String, ItemInfo> getAllItems(GetItemType type) {
        try {
            switch (type) {
                case New -> {
                    PreparedStatement stmt = conn.prepareStatement(
                            "select * from item_fullinfo where name in (select item_name from item_company where company_id = ?) " +
                                    "and delivery_city = ? and state = ? and delivery_staff is null ");
                    stmt.setInt(1, this.id);
                    stmt.setInt(2, getStaffCompany());
                    stmt.setString(3, getStateDatabaseString(ItemState.FromImportTransporting));
                    return getItemsMapping(stmt.executeQuery());
                }
                case OnGoing -> {
                    PreparedStatement stmt = conn.prepareStatement(
                            "select * from item_fullinfo where" +
                                    "(retrieval_staff = ? and state in (?, ?)) " +
                                    "or (delivery_staff = ? and state in (?, ?))");
                    setParas(stmt);
                    return getItemsMapping(stmt.executeQuery());
                }
                case Finished -> {
                    PreparedStatement stmt = conn.prepareStatement(
                            "select * from item_fullinfo where" +
                                    "(retrieval_staff = ? and state not in (?, ?)) " +
                                    "or (delivery_staff = ? and state not in (?, ?))");
                    setParas(stmt);
                    return getItemsMapping(stmt.executeQuery());
                }
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setParas(PreparedStatement stmt) throws SQLException {
        stmt.setString(1, getStaffName());
        stmt.setString(2, getStateDatabaseString(ItemState.PickingUp));
        stmt.setString(3, getStateDatabaseString(ItemState.ToExportTransporting));
        stmt.setString(4, getStaffName());
        stmt.setString(5, getStateDatabaseString(ItemState.FromImportTransporting));
        stmt.setString(6, getStateDatabaseString(ItemState.Delivering));
    }
}
