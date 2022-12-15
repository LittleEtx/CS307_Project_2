package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import cs307.project2.interfaces.ItemInfo;
import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

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
     * information of the current staff
     */
    protected StaffInfo staffInfo;
    protected int id;

    protected User(Connection conn, int id, StaffInfo info) {
        this.id = id;
        this.conn = conn;
        this.staffInfo = info;
        if (this.getStaffType() != info.basicInfo().type()) {
            throw new IllegalArgumentException("User type does not match");
        }
    }

    abstract public LogInfo.StaffType getStaffType();

    public boolean changePassword(String newPassword) {
        //TODO: (optional) change password
        return false;
    }

    public boolean changePhoneNumber(String newPhoneNumber) {
        //TODO: (optional) change phone number
        return false;
    }

    protected Map<String, ItemInfo> getItems(String condition) {
        Map<String, ItemInfo> items = new HashMap<>();
        try {
            var stmt = conn.prepareStatement(
                    "select *\n" +
                            "from (select name, price, class from item " + condition + ") as item\n" +
                            "         left join (select item_name, state from item_state) as item_state on item.name = item_state.item_name\n" +
                            "         left join (select item_name, city_id retrieval_city_id from item_route where stage = 'RETRIEVAL') as retrieval_city\n" +
                            "                   on item.name = retrieval_city.item_name\n" +
                            "         left join (select item_name, city_id import_city_id from item_route where stage = 'IMPORT') as import_city\n" +
                            "                   on item.name = import_city.item_name\n" +
                            "         left join (select item_name, city_id export_city_id from item_route where stage = 'EXPORT') as export_city\n" +
                            "                   on item.name = export_city.item_name\n" +
                            "         left join (select item_name, city_id delivery_city_id from item_route where stage = 'DELIVERY') as delivery_city\n" +
                            "                   on item.name = delivery_city.item_name\n" +
                            "         left join (select item_name, staff_id from staff_handle_item where stage = 'RETRIEVAL') as retrieval\n" +
                            "                   on item.name = retrieval.item_name\n" +
                            "         left join (select id, name retrieval_staff from staff) as retrieval_staff on retrieval.staff_id = retrieval_staff.id\n" +
                            "         left join (select item_name, staff_id from staff_handle_item where stage = 'IMPORT') as import\n" +
                            "                   on item.name = import.item_name\n" +
                            "         left join (select id, name import_staff from staff) as import_staff on import.staff_id = import_staff.id\n" +
                            "         left join (select item_name, staff_id from staff_handle_item where stage = 'EXPORT') as export\n" +
                            "                   on item.name = export.item_name\n" +
                            "         left join (select id, name export_staff from staff) as export_staff on export.staff_id = export_staff.id\n" +
                            "         left join (select item_name, staff_id from staff_handle_item where stage = 'DELIVERY') as delivery\n" +
                            "                   on item.name = delivery.item_name\n" +
                            "         left join (select id, name delivery_staff from staff) as delivery_staff on delivery.staff_id = delivery_staff.id;"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.put(rs.getString("name"), new ItemInfo(
                        rs.getString("name"),
                        rs.getString("class"),
                        rs.getDouble("price"),
                        DatabaseMapping.getItemState(rs.getString("state")),
                        new ItemInfo.RetrievalDeliveryInfo(
                                GlobalQuery.getCityName(rs.getInt("retrieval_city_id")),
                                rs.getString("retrieval_staff")
                        ),
                        new ItemInfo.RetrievalDeliveryInfo(
                                GlobalQuery.getCityName(rs.getInt("delivery_city_id")),
                                rs.getString("delivery_staff")
                        ),
                        new ItemInfo.ImportExportInfo(
                                GlobalQuery.getCityName(rs.getInt("import_city_id")),
                                rs.getString("import_staff"),
                                GlobalQuery.getCityTaxRate(rs.getInt("import_city_id"),
                                        rs.getString("class")).import_rate
                        ),
                        new ItemInfo.ImportExportInfo(
                                GlobalQuery.getCityName(rs.getInt("export_city_id")),
                                rs.getString("export_staff"),
                                GlobalQuery.getCityTaxRate(rs.getInt("export_city_id"),
                                        rs.getString("class")).export_rate
                        )
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
