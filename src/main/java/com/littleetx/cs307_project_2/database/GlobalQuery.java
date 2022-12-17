package com.littleetx.cs307_project_2.database;

import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import main.interfaces.LogInfo;
import main.interfaces.StaffInfo;

import java.sql.*;
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
                        info.getUrl(), info.username(), info.password());
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to database", e);
            }
        }
    }

    public static Connection getRootConnection() {
        return RootConnectionGetter.instance;
    }


    /**
     * Note: this method will query the database, do not use too frequently
     */
    public static StaffInfo getStaffInfo(Connection conn, int id) {
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

    public static StaffInfo getStaffInfo(int id) {
        return getStaffInfo(getRootConnection(), id);
    }


    public static int getStaffId(String name) {
        Connection conn = getRootConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("select id from staff where name= ? ");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
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

    public static TaxInfo.Value getCityTaxRate(int cityID, String item_type) {
        return getCityTaxRate(new TaxInfo.Key(cityID, item_type));
    }

    public static TaxInfo.Value getCityTaxRate(TaxInfo.Key key) {
        return TaxRateGetter.taxRates.get(key);
    }

    private static class TaxRateGetter {
        private static final Map<TaxInfo.Key, TaxInfo.Value> taxRates;

        static {
            try (PreparedStatement stmt = getRootConnection()
                    .prepareStatement("SELECT * FROM tax_info")) {
                taxRates = new HashMap<>();
                var result = stmt.executeQuery();
                while (result.next()) {
                    taxRates.put(new TaxInfo.Key(
                                    result.getInt("city_id"),
                                    result.getString("item_type")),
                            new TaxInfo.Value(
                                    result.getDouble("export_rate"),
                                    result.getDouble("import_rate")
                            ));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
