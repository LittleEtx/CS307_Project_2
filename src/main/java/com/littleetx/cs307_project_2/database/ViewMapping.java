package com.littleetx.cs307_project_2.database;

import main.interfaces.ItemInfo;
import main.interfaces.LogInfo;
import main.interfaces.ShipInfo;
import main.interfaces.StaffInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.littleetx.cs307_project_2.database.DatabaseMapping.getGender;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.getStaffAuthority;
import static com.littleetx.cs307_project_2.database.GlobalQuery.getCityName;
import static com.littleetx.cs307_project_2.database.GlobalQuery.getCompanyName;

public class ViewMapping {

    public static Map<String, ItemInfo> getItemsMapping(ResultSet rs) {
        Map<String, ItemInfo> items = new HashMap<>();
        try {
            while (rs.next()) {
                items.put(rs.getString("name"), new ItemInfo(
                        rs.getString("name"),
                        rs.getString("class"),
                        rs.getDouble("price"),
                        DatabaseMapping.getItemState(rs.getString("state")),
                        new ItemInfo.RetrievalDeliveryInfo(
                                getCityName(rs.getInt("retrieval_city")),
                                rs.getString("retrieval_staff")
                        ),
                        new ItemInfo.RetrievalDeliveryInfo(
                                getCityName(rs.getInt("delivery_city")),
                                rs.getString("delivery_staff")
                        ),
                        new ItemInfo.ImportExportInfo(
                                getCityName(rs.getInt("import_city")),
                                rs.getString("import_staff"),
                                rs.getDouble("price") * GlobalQuery.getCityTaxRate(rs.getInt("import_city"),
                                        rs.getString("class")).import_rate
                        ),
                        new ItemInfo.ImportExportInfo(
                                getCityName(rs.getInt("export_city")),
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

    public static Map<Integer, StaffInfo> getStaffsMapping(ResultSet rs) {
        Map<Integer, StaffInfo> staffs = new HashMap<>();
        try {
            while (rs.next()) {
                staffs.put(rs.getInt("id"), new StaffInfo(
                        new LogInfo(
                                rs.getString("name"),
                                getStaffAuthority(rs.getString("authority")),
                                rs.getString("password")
                        ),
                        getCompanyName(rs.getInt("company")),
                        getCityName(rs.getInt("city")),
                        getGender(rs.getString("gender")),
                        rs.getDate("birth").toLocalDate()
                                .until(LocalDate.now()).getYears(),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return staffs;
    }

    public static Map<String, ShipInfo> getShipsMapping(ResultSet rs) {
        Map<String, ShipInfo> ships = new HashMap<>();
        try {
            while (rs.next()) {
                ships.put(rs.getString("name"), new ShipInfo(
                        rs.getString("name"),
                        GlobalQuery.getCompanyName(rs.getInt("company")),
                        DatabaseMapping.getIsSailing(rs.getString("state"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ships;
    }
}
