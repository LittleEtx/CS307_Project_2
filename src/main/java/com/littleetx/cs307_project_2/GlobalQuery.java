package com.littleetx.cs307_project_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GlobalQuery {
    private static class RootConnectionGetter {
        private static final Connection instance;
        static  {
            var info = LoginInfoGetter.getLoginInfo();
            try {
                instance = DriverManager.getConnection(
                LoginInfoGetter.getUrl(info,true),info.username(),info.password());
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to database", e);
            }
        }
    }

    public static Connection getRootConnection() {
        return RootConnectionGetter.instance;
    }

    private static class StaffsGetter {
        private static final Map<Integer, String> staffs;
        static {
            try (PreparedStatement stmt = getRootConnection()
                    .prepareStatement("SELECT * FROM staff")) {
                staffs = new HashMap<>();
                var result = stmt.executeQuery();
                while (result.next()) {
                    staffs.put(result.getInt("id"),
                            result.getString("name"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getStaffName(int id) {
        return StaffsGetter.staffs.get(id);
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


}
