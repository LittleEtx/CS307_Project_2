package com.littleetx.cs307_project_2;

import com.littleetx.cs307_project_2.file_reader.FileOperator;
import com.littleetx.cs307_project_2.file_reader.FileOperator_CSV;
import com.littleetx.cs307_project_2.file_reader.SQLReader;
import com.littleetx.cs307_project_2.user.*;
import cs307.project2.interfaces.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;

import static com.littleetx.cs307_project_2.CSVMapping.*;

public class DatabaseManipulation implements IDatabaseManipulation {
    private final Connection rootConn;
    private final Verification<CompanyManager> companyManagers;
    private final Verification<SeaportOfficer> seaportOfficers;
    private final Verification<Courier> couriers;
    private final Verification<SustcManager> sustcManagers;

    private static final String CREATE_DATABASE_AND_USERS_SQL = "scripts/CreateDatabaseAndUsers.sql";
    private static final String DDL_SQL = "scripts/DDl.sql";
    private static final int PACKET_SIZE = 1000;

    public DatabaseManipulation(String database, String root, String pass) {
        this(database, root, pass, true);
    }

    public DatabaseManipulation(String database, String root, String pass, boolean initDatabase) {
        try {
            rootConn = DriverManager.getConnection(database, root, pass);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }

        if (initDatabase) {
            // Create database and users
            try (SQLReader reader = new SQLReader(new File(CREATE_DATABASE_AND_USERS_SQL))) {
                for (String sql : reader) {
                    rootConn.createStatement().execute(sql);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create database and users", e);
            }

            // Create tables
            try (SQLReader reader = new SQLReader(new File(DDL_SQL))) {
                for (String sql : reader) {
                    rootConn.createStatement().execute(sql);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create tables", e);
            }

            //TODO: move the rootConn to new database
        }

        //TODO: create new users and create verification factory
        //temporary use root users
        companyManagers = new Verification<>(rootConn, CompanyManager.class);
        seaportOfficers = new Verification<>(rootConn, SeaportOfficer.class);
        couriers = new Verification<>(rootConn, Courier.class);
        sustcManagers = new Verification<>(rootConn, SustcManager.class);
    }

    @Override
    public void $import(String recordsCSV, String staffsCSV) {
        //read staffs
        FileOperator fileOperator = new FileOperator_CSV(staffsCSV);
        try {
            var reader = fileOperator.getReader();
            Map<StaffInfo, Integer> staffs = new HashMap<>();
            int staffCount = 0;
            Map<String, Integer> companies = new HashMap<>();
            int companyCount = 0;
            Map<String, Integer> cities = new HashMap<>();
            int cityCount = 0;
            List<int[]> staff_company = new ArrayList<>();
            List<int[]> staff_city = new ArrayList<>();
            Iterator<String[]> iterator = reader.iterator();
            //drop first line
            iterator.next();
            while (iterator.hasNext()) {
                String[] line = iterator.next();


                Integer companyId = companies.get(line[STAFF_COMPANY]);
                if (line[STAFF_COMPANY] != null && companyId == null) {
                    companyId = ++companyCount;
                    companies.put(line[STAFF_COMPANY], companyId);
                }
                Integer cityId = cities.get(line[STAFF_CITY]);
                if (line[STAFF_CITY] != null && cityId == null) {
                    cityId = ++cityCount;
                    cities.put(line[STAFF_CITY], cityId);
                }

                int staffId = 10000000 +
                        Objects.requireNonNullElse(companyId, 0) * 100000 +
                        ++staffCount;
                if (companyId != null) {
                    staff_company.add(new int[]{staffId, companyId});
                }
                if (cityId != null) {
                    staff_city.add(new int[]{staffId, cityId});
                }

                LogInfo logInfo = new LogInfo(
                        line[STAFF_NAME], getStaffType(line[STAFF_TYPE]), line[STAFF_PASSWORD]);
                StaffInfo staffInfo = new StaffInfo(
                        logInfo, line[STAFF_COMPANY], line[STAFF_CITY],
                        "female".equals(line[STAFF_GENDER]), Integer.parseInt(line[STAFF_AGE]),
                        line[STAFF_PHONE]);
                staffs.put(staffInfo, staffId);
            }

            //insert to tables
            insertData(rootConn.prepareStatement("INSERT INTO Company VALUES (?, ?)"),
                    companies.keySet(), (stmt, company) -> {
                        try {
                            stmt.setInt(1, companies.get(company));
                            stmt.setString(2, company);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            insertData(rootConn.prepareStatement("INSERT INTO City VALUES (?, ?)"),
                    cities.keySet(), (stmt, city) -> {
                        try {
                            stmt.setInt(1, cities.get(city));
                            stmt.setString(2, city);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });

            insertData(rootConn.prepareStatement("INSERT INTO Staff VALUES (?, ?, ?, ?, ?)"),
                    staffs.keySet(), (stmt, staff) -> {
                        try {
                            stmt.setInt(1, staffs.get(staff));
                            stmt.setString(2, staff.basicInfo().name());
                            stmt.setString(3, staff.isFemale() ? "FEMALE" : "MALE");
                            stmt.setDate(4, Date.valueOf(LocalDate.ofYearDay(
                                    LocalDate.now().getYear() - staff.age(), 1)));
                            stmt.setString(5, staff.phoneNumber());
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });

            insertData(rootConn.prepareStatement("INSERT INTO verification VALUES (?, ?, ?)"),
                    staffs.keySet(), (stmt, staff) -> {
                        try {
                            stmt.setInt(1, staffs.get(staff));
                            stmt.setString(2, getStaffAuthority(staff.basicInfo().type()));
                            stmt.setString(3, staff.basicInfo().password());
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });

            insertData(rootConn.prepareStatement("INSERT INTO staff_company VALUES (?, ?)"),
                    staff_company, (stmt, pair) -> {
                        try {
                            stmt.setInt(1, pair[0]);
                            stmt.setInt(2, pair[1]);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });

            insertData(rootConn.prepareStatement("INSERT INTO staff_city VALUES (?, ?)"),
                    staff_city, (stmt, pair) -> {
                        try {
                            stmt.setInt(1, pair[0]);
                            stmt.setInt(2, pair[1]);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to open file", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert data", e);
        }
    }

    private <T> void insertData(PreparedStatement statement, Collection<T> data,
                                BiConsumer<PreparedStatement, T> setPara) {
        int count = 0;
        try{
            for (T t: data) {
                setPara.accept(statement, t);
                count++;
                statement.addBatch();
                if (count % PACKET_SIZE == 0){
                    statement.executeBatch();
                    statement.clearBatch();
                }
            }
            if (count % PACKET_SIZE != 0){
                statement.executeBatch();
            }
            statement.clearBatch();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public double getImportTaxRate(LogInfo log, String city, String itemClass) {
        var user = companyManagers.getUser(log);
        return user != null ? user.getTaxRate(city, itemClass, CompanyManager.TaxType.Import) : -1;
    }

    @Override
    public double getExportTaxRate(LogInfo log, String city, String itemClass) {
        var user = companyManagers.getUser(log);
        return user != null ? user.getTaxRate(city, itemClass, CompanyManager.TaxType.Export) : -1;
    }

    @Override
    public boolean loadItemToContainer(LogInfo log, String itemName, String containerCode) {
        var user = companyManagers.getUser(log);
        return user != null && user.loadItemToContainer(itemName, containerCode);
    }

    @Override
    public boolean loadContainerToShip(LogInfo log, String shipName, String containerCode) {
        var user = companyManagers.getUser(log);
        return user != null && user.loadContainerToShip(shipName, containerCode);
    }

    @Override
    public boolean shipStartSailing(LogInfo log, String shipName) {
        var user = companyManagers.getUser(log);
        return user != null && user.shipStartSailing(shipName);
    }

    @Override
    public boolean unloadItem(LogInfo log, String itemName) {
        var user = companyManagers.getUser(log);
        return user != null && user.unloadItem(itemName);
    }

    @Override
    public boolean itemWaitForChecking(LogInfo log, String item) {
        var user = companyManagers.getUser(log);
        return user != null && user.itemWaitForChecking(item);
    }

    @Override
    public boolean newItem(LogInfo log, ItemInfo item) {
        var user = couriers.getUser(log);
        return user != null && user.newItem(item);
    }

    @Override
    public boolean setItemState(LogInfo log, String itemName, ItemState state) {
        var user = couriers.getUser(log);
        return user != null && user.setItemState(itemName, state);
    }

    @Override
    public String[] getAllItemsAtPort(LogInfo log) {
        var user = seaportOfficers.getUser(log);
        return user != null ? user.getAllItemsAtPort() : null;
    }

    @Override
    public boolean setItemCheckState(LogInfo log, String itemName, boolean success) {
        var user = seaportOfficers.getUser(log);
        return user != null && user.setItemCheckState(itemName, success);
    }

    @Override
    public int getCompanyCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.Company) : -1;
    }

    @Override
    public int getCityCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.City) : -1;
    }

    @Override
    public int getCourierCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.Courier) : -1;
    }

    @Override
    public int getShipCount(LogInfo log) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getCount(SustcManager.CountType.Ship) : -1;
    }

    @Override
    public ItemInfo getItemInfo(LogInfo log, String name) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getItemInfo(name) : null;
    }

    @Override
    public ShipInfo getShipInfo(LogInfo log, String name) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getShipInfo(name) : null;
    }

    @Override
    public ContainerInfo getContainerInfo(LogInfo log, String code) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getContainerInfo(code) : null;
    }

    @Override
    public StaffInfo getStaffInfo(LogInfo log, String name) {
        var user = sustcManagers.getUser(log);
        return user != null ? user.getStaffInfo(name) : null;
    }
}
