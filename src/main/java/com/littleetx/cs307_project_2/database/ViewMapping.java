package com.littleetx.cs307_project_2.database;

import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import main.interfaces.*;

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
                items.put(rs.getString("name"), getItemInfo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    private static ItemInfo getItemInfo(ResultSet rs) throws SQLException {
        return new ItemInfo(
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
        );
    }

    public static Map<String, ItemFullInfo> getItemFullInfoMapping(ResultSet rs) {
        Map<String, ItemFullInfo> items = new HashMap<>();
        try {
            while (rs.next()) {
                items.put(rs.getString("name"), new ItemFullInfo(
                        getItemInfo(rs),
                        rs.getString("ship_name"),
                        rs.getString("container_code"),
                        DatabaseMapping.getContainerType(rs.getString("container_type"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public static Map<Integer, StaffInfo> getStaffsMapping(ResultSet rs) {
        return getStaffsMapping(rs, false);
    }

    public static Map<Integer, StaffInfo> getStaffsMapping(ResultSet rs, boolean showPassword) {
        Map<Integer, StaffInfo> staffs = new HashMap<>();
        try {
            while (rs.next()) {
                staffs.put(rs.getInt("id"), new StaffInfo(
                        new LogInfo(
                                rs.getString("name"),
                                getStaffAuthority(rs.getString("authority")),
                                showPassword ? rs.getString("password") : null
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

    public static Map<String, ContainerInfo> getContainersMapping(ResultSet rs) {
        Map<String, ContainerInfo> containers = new HashMap<>();
        try {

            while (rs.next()) {
                containers.put(rs.getString("code"), new ContainerInfo(
                        DatabaseMapping.getContainerType(rs.getString("type")),
                        rs.getString("code"),
                        DatabaseMapping.getIsContainerUsing(rs.getString("state"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return containers;
    }
}
