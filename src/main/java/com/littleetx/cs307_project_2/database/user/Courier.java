package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import main.interfaces.ItemInfo;
import main.interfaces.ItemState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Courier extends User {
    private static final ItemState[] retrievalStates = {
            ItemState.PickingUp,
            ItemState.ToExportTransporting,
    };

    private static final ItemState[] deliveryStates = {
            ItemState.FromImportTransporting,
            ItemState.Delivering,
    };

    private static String getList(ItemState[] states) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(DatabaseMapping.getStateDatabaseString(states[0]));
        sb.append("'");
        for (int i = 1; i < states.length; i++) {
            sb.append(", '");
            sb.append(DatabaseMapping.getStateDatabaseString(states[i]));
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
        //TODO
        try {
            PreparedStatement stmt=conn.prepareStatement("select * from item where name= ? ");
            stmt.setString(1,item.name());
            ResultSet rs=stmt.executeQuery();
            if (!rs.next()){
                stmt=conn.prepareStatement("insert into item values(?,?,?)");
                stmt.setString(1,item.name());
                stmt.setInt(2, (int) item.price());
                stmt.setString(3,item.$class());
                stmt.execute();

                stmt=conn.prepareStatement("insert into item_route values(?,?,?),(?,?,?),(?,?,?),(?,?,?)");
                stmt.setString(1,item.name());
                stmt.setInt(2, GlobalQuery.getCityID(item.retrieval().city()));
                stmt.setString(3,"RETRIEVAL");
                stmt.setString(4,item.name());
                stmt.setInt(5, GlobalQuery.getCityID(item.export().city()));
                stmt.setString(6,"EXPORT");
                stmt.setString(7,item.name());
                stmt.setInt(8, GlobalQuery.getCityID(item.$import().city()));
                stmt.setString(9,"IMPORT");
                stmt.setString(10,item.name());
                stmt.setInt(11, GlobalQuery.getCityID(item.delivery().city()));
                stmt.setString(12,"DELIVERY");
                stmt.execute();

                stmt= conn.prepareStatement("insert into item_state values(?,?)");
                stmt.setString(1,item.name());
                stmt.setString(2,DatabaseMapping.getStateDatabaseString(ItemState.PickingUp));
                stmt.execute();

                stmt= conn.prepareStatement("insert into staff_handle_item values(?,?,?)");
                stmt.setString(1,item.name());
                stmt.setInt(2,GlobalQuery.getStaffId(item.retrieval().courier()));
                stmt.setString(3,"RETRIEVAL");
                stmt.execute();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Set the item’s current state to the given state with given item name.
     *
     * @return false if item already exist or s is illegal
     */
    public boolean setItemState(String itemName, ItemState itemState) {
        //TODO
        return false;
    }

    public enum GetItemType {
        New, OnGoing, Finished
    }

    public Map<String, ItemInfo> getAllItems(GetItemType type) {
        switch (type) {
            case New -> {
                return getItems("where name in (\n" +
                        "select item_name from item_route where stage = 'DELIVERY' and city_id in \n" +
                        "(select city_id from staff_city where staff_id = " + id + ") intersect\n" +
                        "select item_name from item_state where state = 'FROM_IMPORT_TRANSPORTING' except\n" +
                        "select item_name from staff_handle_item where stage = 'DELIVERY')");
            }
            case OnGoing -> {
                return getItems("where name in (\n" +
                        "(select item_name from item_state where state in (" + getList(retrievalStates) + ")\n" +
                        "intersect select item_name from staff_handle_item where stage = 'RETRIEVAL' and staff_id = " + id + ")\n" +
                        "union\n" +
                        "(select item_name from item_state where state in (" + getList(deliveryStates) + ")\n" +
                        "intersect select item_name from staff_handle_item where stage = 'DELIVERY' and staff_id = " + id + "))");
            }
            case Finished -> {
                return getItems("where name in (\n" +
                        "(select item_name from item_state where state not in (" + getList(retrievalStates) + ")\n" +
                        "intersect select item_name from staff_handle_item where stage = 'RETRIEVAL' and staff_id = " + id + ")\n" +
                        "union\n" +
                        "(select item_name from item_state where state not in (" + getList(deliveryStates) + ")\n" +
                        "intersect select item_name from staff_handle_item where stage = 'DELIVERY' and staff_id = " + id + "))");
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
