package com.littleetx.cs307_project_2.database;

import com.littleetx.cs307_project_2.database.database_type.CityInfo;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import main.interfaces.StaffInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static com.littleetx.cs307_project_2.database.ViewMapping.getStaffsMapping;

public class GlobalQuery {
    private static class RootConnectionGetter {
        private static final Connection instance;

        static {
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
    public static StaffInfo getStaffInfo(int id) {
        Connection conn = getRootConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM staff_info where id = ?");
            stmt.setInt(1, id);
            var result = getStaffsMapping(stmt.executeQuery(), true);
            return result.isEmpty() ? null : result.values().iterator().next();
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

    public static Map<Integer, String> getCompanies() {
        return Collections.unmodifiableMap(CompaniesGetter.companies);
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
        private static final Map<Integer, CityInfo> cities;

        static {
            try {
                PreparedStatement stmt = getRootConnection()
                        .prepareStatement("SELECT id, name, max(export_rate) rate " +
                                "FROM city left join tax_info on city.id = tax_info.city_id group by id");
                cities = new HashMap<>();
                var result = stmt.executeQuery();
                while (result.next()) {
                    cities.put(result.getInt("id"),
                            new CityInfo(
                                    result.getString("name"),
                                    result.getDouble("rate") != 0
                            ));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Map<Integer, CityInfo> getCities() {
        return Collections.unmodifiableMap(CityGetter.cities);
    }

    public static String getCityName(int id) {
        return id > 0 ? CityGetter.cities.get(id).name() : null;
    }

    public static CityInfo getCity(int id) {
        return CityGetter.cities.get(id);
    }

    public static int getCityID(String name) {
        if (name == null) {
            return -1;
        }

        for (Map.Entry<Integer, CityInfo> entry : CityGetter.cities.entrySet()) {
            if (entry.getValue().name().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static TaxInfo.Value getCityTaxRate(int cityID, String itemType) {
        return getCityTaxRate(new TaxInfo.Key(cityID, itemType));
    }

    public static TaxInfo.Value getCityTaxRate(TaxInfo.Key key) {
        return TaxRateGetter.taxRates.get(key);
    }

    public static Map<TaxInfo.Key, TaxInfo.Value> getCityTaxRates() {
        return Collections.unmodifiableMap(TaxRateGetter.taxRates);
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

    private static class ItemTypeGetter {
        private static final Set<String> itemTypes;

        static {
            try (PreparedStatement stmt = getRootConnection()
                    .prepareStatement("SELECT * FROM item_type")) {
                itemTypes = new HashSet<>();
                var result = stmt.executeQuery();
                while (result.next()) {
                    itemTypes.add(result.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Set<String> getItemTypes() {
        return ItemTypeGetter.itemTypes;
    }

}
