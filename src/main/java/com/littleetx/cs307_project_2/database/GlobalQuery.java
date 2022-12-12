package com.littleetx.cs307_project_2.database;

import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class GlobalQuery {
    private static class RootConnectionGetter {
        private static final Connection instance;
        static  {
            var info = DatabaseLoginInfo.getLoginInfo();
            try {
                instance = DriverManager.getConnection(
                        info.getUrl(true), info.username(), info.password());
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to database", e);
            }
        }
    }

    public static Connection getRootConnection() {
        return RootConnectionGetter.instance;
    }


    public static StaffInfo getStaffInfo(int id) {
        Connection conn = getRootConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM staff join verification on staff.id = verification.staff_id " +
                            "left join staff_company on staff.id = staff_company.staff_id " +
                            "left join staff_city on staff.id = staff_city.staff_id " +
                            "WHERE id = ?");
            stmt.setInt(1, id);
            var result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            return new StaffInfo(
                    new LogInfo(
                            result.getString("name"),
                            DatabaseMapping.getStaffAuthority(
                                    result.getString("authority")),
                            result.getString("password")
                    ),
                    getCompanyName(result.getInt("company_id")),
                    getCityName(result.getInt("city_id")),
                    DatabaseMapping.getGender(result.getString("gender")),
                    result.getDate("birth").toLocalDate()
                            .until(LocalDate.now()).getYears(),
                    result.getString("phone_number")
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CompaniesGetter {
        private static final Map<Integer, String> companies;

        static {
            try (PreparedStatement stmt = getRootConnection()
                    .prepareStatement("SELECT * FROM company")) {
                companies = new HashMap<>();
                var result = stmt.executeQuery();
                while (result.next()) {
                    companies.put(result.getInt("id"),
                            result.getString("name"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getCompanyName(int id) {
        return CompaniesGetter.companies.get(id);
    }

    public static int getCompanyID(String name) {
        for (Map.Entry<Integer, String> entry : CompaniesGetter.companies.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private static class CityGetter {
        private static final Map<Integer, String> cities;

        static {
            try (PreparedStatement stmt = getRootConnection()
                    .prepareStatement("SELECT * FROM city")) {
                cities = new HashMap<>();
                var result = stmt.executeQuery();
                while (result.next()) {
                    cities.put(result.getInt("id"),
                            result.getString("name"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getCityName(int id) {
        return CityGetter.cities.get(id);
    }

    public static int getCityID(String name) {
        for (Map.Entry<Integer, String> entry : CityGetter.cities.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }


}
