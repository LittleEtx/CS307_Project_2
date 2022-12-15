package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import cs307.project2.interfaces.ItemInfo;
import cs307.project2.interfaces.ItemState;
import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.sql.Connection;
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
        sb.append(states[0].toString());
        sb.append("'");
        for (int i = 1; i < states.length; i++) {
            sb.append(", '");
            sb.append(DatabaseMapping.getItemState(states[i]));
            sb.append("'");
        }
        return sb.toString();
    }

    public Courier(Connection conn, Integer id, StaffInfo info) {
        super(conn, id, info);
    }

    @Override
    public LogInfo.StaffType getStaffType() {
        return LogInfo.StaffType.Courier;
    }

    /**
     * Add a new item that is currently being picked up. The item shall contain
     * all necessary information(basic info and four cities). Returns false if item
     * already exist or contains illegal information
     */
    public boolean newItem(ItemInfo item) {
        //TODO
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
                return getItems("""
                        where name in (
                        select item_name from item_state where state = 'FROM_IMPORT_TRANSPORTING' except
                        select item_name from staff_handle_item where stage = 'DELIVERY')""");
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
