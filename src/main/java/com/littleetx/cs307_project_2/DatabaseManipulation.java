package com.littleetx.cs307_project_2;

import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
import com.littleetx.cs307_project_2.database.user.SustcManager;
import com.littleetx.cs307_project_2.file_reader.FileOperator;
import com.littleetx.cs307_project_2.file_reader.FileOperator_CSV;
import com.littleetx.cs307_project_2.file_reader.SQLReader;
import cs307.project2.interfaces.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;

import static com.littleetx.cs307_project_2.CSVMapping.*;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.getContainerType;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.getGender;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.getItemState;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.*;

public class DatabaseManipulation implements IDatabaseManipulation {
    private final Connection rootConn;
    private final Verification verification;

    private static final String CREATE_DATABASE_AND_USERS_SQL = "scripts/CreateUsers.sql";
    private static final String DDL_SQL = "scripts/DDl.sql";
    private static final int PACKET_SIZE = 1000;

    public DatabaseManipulation(String database, String root, String pass) {
        this(database, root, pass, true);
    }

    public DatabaseManipulation(String database, String root, String pass, boolean initDatabase) {
        Connection conn;
        try {
            conn = DriverManager.getConnection(database, root, pass);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }

        if (initDatabase) {
            // Create database
            try {
                conn.prepareStatement("create database sustc").execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create database", e);
            }

            database =  database.substring(0, 18) +
                    database.substring(18).split("/")[0] +
                    "/sustc";
            try {
                conn = DriverManager.getConnection(database, root, pass);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get new root user", e);
            }

            // Create tables
            try (SQLReader reader = new SQLReader(new File(DDL_SQL))) {
                for (String sql : reader) {
                    conn.createStatement().execute(sql);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create tables", e);
            }

            // create users
            try (SQLReader reader = new SQLReader(new File(CREATE_DATABASE_AND_USERS_SQL))) {
                for (String sql : reader) {
                    conn.createStatement().execute(sql);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to create database and users", e);
            }
        }
        rootConn = conn;
        verification = new Verification(database);
    }

    @Override
    public void $import(String recordsCSV, String staffsCSV) {
        //read staffs
        FileOperator staffFile = new FileOperator_CSV(staffsCSV);
        FileOperator recordFile = new FileOperator_CSV(recordsCSV);
        try {
            var staffReader = staffFile.getReader();
            Map<StaffInfo, Integer> staffs = new HashMap<>();
            Map<String, Integer> staffMap = new HashMap<>();
            int staffCount = 0;
            Map<String, Integer> companies = new HashMap<>();
            int companyCount = 0;
            Map<String, Integer> cities = new HashMap<>();
            int cityCount = 0;
            List<int[]> staff_company = new ArrayList<>();
            List<int[]> staff_city = new ArrayList<>();
            Iterator<String[]> staffIterator = staffReader.iterator();
            //drop first line
            staffIterator.next();
            while (staffIterator.hasNext()) {
                String[] line = staffIterator.next();

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
                        CSVMapping.getGender(line[STAFF_GENDER]), Integer.parseInt(line[STAFF_AGE]),
                        line[STAFF_PHONE]);
                staffs.put(staffInfo, staffId);
                staffMap.put(line[STAFF_NAME], staffId);
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
            Debug.println("Loaded data into company");

            insertData(rootConn.prepareStatement("INSERT INTO City VALUES (?, ?)"),
                    cities.keySet(), (stmt, city) -> {
                        try {
                            stmt.setInt(1, cities.get(city));
                            stmt.setString(2, city);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into city");

            insertData(rootConn.prepareStatement("INSERT INTO Staff VALUES (?, ?, ?, ?, ?)"),
                    staffs.keySet(), (stmt, staff) -> {
                        try {
                            stmt.setInt(1, staffs.get(staff));
                            stmt.setString(2, staff.basicInfo().name());
                            stmt.setString(3, getGender(staff.isFemale()));
                            stmt.setDate(4, Date.valueOf(LocalDate.ofYearDay(
                                    LocalDate.now().getYear() - staff.age(), 1)));
                            stmt.setString(5, staff.phoneNumber());
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into staff");

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
            Debug.println("Loaded data into verification");

            insertData(rootConn.prepareStatement("INSERT INTO staff_company VALUES (?, ?)"),
                    staff_company, (stmt, pair) -> {
                        try {
                            stmt.setInt(1, pair[0]);
                            stmt.setInt(2, pair[1]);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into staff_company");

            insertData(rootConn.prepareStatement("INSERT INTO staff_city VALUES (?, ?)"),
                    staff_city, (stmt, pair) -> {
                        try {
                            stmt.setInt(1, pair[0]);
                            stmt.setInt(2, pair[1]);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into staff_city");

            var recordReader = recordFile.getReader();
            Iterator<String[]> recordIterator = recordReader.iterator();

            Set<String> itemTypes = new HashSet<>();
            List<ItemInfo> items = new ArrayList<>();
            Map<TaxInfo.Key, TaxInfo.Value> taxes = new HashMap<>();
            Map<String, ShipInfo> ships = new HashMap<>();
            Set<ContainerInfo> containers = new HashSet<>();
            Map<String, String> item_container = new HashMap<>();
            Map<String, String> container_ship = new HashMap<>();

            //drop first line
            recordIterator.next();
            while (recordIterator.hasNext()) {
                String[] line = recordIterator.next();

                itemTypes.add(line[ITEM_CLASS]);
                ItemInfo itemInfo = new ItemInfo(
                                line[ITEM_NAME], line[ITEM_CLASS],
                                Integer.parseInt(line[ITEM_PRICE]),
                                CSVMapping.getItemState(line[ITEM_STATE]),
                                new ItemInfo.RetrievalDeliveryInfo(
                                        line[RETRIEVAL_CITY], line[RETRIEVAL_COURIER]
                                ),
                                new ItemInfo.RetrievalDeliveryInfo(
                                        line[DELIVERY_CITY], line[DELIVERY_COURIER]
                                ),
                                new ItemInfo.ImportExportInfo(
                                        line[IMPORT_CITY], line[IMPORT_OFFICER],
                                        Double.parseDouble(line[IMPORT_TAX])
                                ),
                        new ItemInfo.ImportExportInfo(
                                line[EXPORT_CITY], line[EXPORT_OFFICER],
                                Double.parseDouble(line[EXPORT_TAX])
                        ));
                items.add(itemInfo);
                TaxInfo.Key exportKey = new TaxInfo.Key(cities.get(line[EXPORT_CITY]), line[ITEM_CLASS]);
                if (!taxes.containsKey(exportKey)) {
                    taxes.put(exportKey, new TaxInfo.Value(null, null));
                }
                taxes.get(exportKey).export_rate = Math.max(Objects.requireNonNullElse(
                        taxes.get(exportKey).export_rate, -1.0), itemInfo.export().tax() / itemInfo.price());
                TaxInfo.Key importKey = new TaxInfo.Key(cities.get(line[IMPORT_CITY]), line[ITEM_CLASS]);
                if (!taxes.containsKey(importKey)) {
                    taxes.put(importKey, new TaxInfo.Value(null, null));
                }
                taxes.get(importKey).import_rate = Math.max(Objects.requireNonNullElse(
                        taxes.get(importKey).import_rate, -1.0), itemInfo.$import().tax() / itemInfo.price());

                if (line[CONTAINER_CODE] != null) {
                    ContainerInfo containerInfo = new ContainerInfo(
                            CSVMapping.getContainerType(line[CONTAINER_TYPE]), line[CONTAINER_CODE], false);
                    containers.add(containerInfo);
                    item_container.put(line[ITEM_NAME], line[CONTAINER_CODE]);
                }

                if (line[SHIP_NAME] != null) {
                    ShipInfo shipInfo = new ShipInfo(
                            line[SHIP_NAME], line[COMPANY_NAME],
                            itemInfo.state() == ItemState.Shipping);
                    if (ships.get(line[SHIP_NAME]) == null || shipInfo.sailing()) {
                        ships.put(line[SHIP_NAME], shipInfo);
                    }
                    ships.put(line[SHIP_NAME], shipInfo);
                    container_ship.put(line[CONTAINER_CODE], line[SHIP_NAME]);
                }
            }

            insertData(rootConn.prepareStatement("INSERT INTO item_type VALUES (?)"),
                    itemTypes, (stmt, type) -> {
                        try {
                            stmt.setString(1, type);
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into item_type");

            insertData(rootConn.prepareStatement("insert into item values (?, ?, ?)"),
                    items, (stmt, item) -> {
                        try {
                            stmt.setString(1, item.name());
                            stmt.setInt(2, (int) item.price());
                            stmt.setString(3, item.$class());
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into item");

            insertData(rootConn.prepareStatement("insert into item_state values (?, ?)"),
                    items, (stmt, item) -> {
                        try {
                            stmt.setString(1, item.name());
                            stmt.setString(2, getItemState(item.state()));
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into item_state");

            insertData(rootConn.prepareStatement("insert into staff_handle_item values (?, ?, ?)"),
                    items, (stmt, item) -> {
                        try {
                            stmt.setString(1, item.name());
                            stmt.setInt(2, staffMap.get(item.retrieval().courier()));
                            stmt.setString(3, "RETRIEVAL");
                            if (item.export().officer() != null) {
                                stmt.addBatch();
                                stmt.setString(1, item.name());
                                stmt.setInt(2, staffMap.get(item.export().officer()));
                                stmt.setString(3, "EXPORT");
                            }
                            if (item.$import().officer() != null) {
                                stmt.addBatch();
                                stmt.setString(1, item.name());
                                stmt.setInt(2, staffMap.get(item.$import().officer()));
                                stmt.setString(3, "IMPORT");
                            }
                            if (item.delivery().courier() != null) {
                                stmt.addBatch();
                                stmt.setString(1, item.name());
                                stmt.setInt(2, staffMap.get(item.delivery().courier()));
                                stmt.setString(3, "DELIVERY");
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into staff_handle_item");

            insertData(rootConn.prepareStatement("insert into item_route values (?, ?, ?)"),
                    items, (stmt, item) -> {
                        try {
                            stmt.setString(1, item.name());
                            stmt.setInt(2, cities.get(item.retrieval().city()));
                            stmt.setString(3, "RETRIEVAL");
                            stmt.addBatch();
                            stmt.setString(1, item.name());
                            stmt.setInt(2, cities.get(item.export().city()));
                            stmt.setString(3, "EXPORT");
                            stmt.addBatch();
                            stmt.setString(1, item.name());
                            stmt.setInt(2, cities.get(item.$import().city()));
                            stmt.setString(3, "IMPORT");
                            stmt.addBatch();
                            stmt.setString(1, item.name());
                            stmt.setInt(2, cities.get(item.delivery().city()));
                            stmt.setString(3, "DELIVERY");
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data to item_route");

            insertData(rootConn.prepareStatement("insert into tax_info values (?, ?, ?, ?)"),
                    taxes.entrySet(), (stmt, entry) -> {
                        try {
                            stmt.setString(1, entry.getKey().item_type());
                            stmt.setInt(2, entry.getKey().cityId());
                            if (entry.getValue().import_rate >= 0) {
                                stmt.setDouble(3, entry.getValue().import_rate);
                            } else {
                                stmt.setNull(3, Types.DOUBLE);
                            }
                            if (entry.getValue().export_rate >= 0) {
                                stmt.setDouble(4, entry.getValue().export_rate);
                            } else {
                                stmt.setNull(4, Types.DOUBLE);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into tax_info");

            insertData(rootConn.prepareStatement("insert into ship values (?, ?)"),
                    ships.values(), (stmt, ship) -> {
                        try {
                            stmt.setString(1, ship.name());
                            stmt.setInt(2, companies.get(ship.owner()));
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into ship");

            insertData(rootConn.prepareStatement("insert into ship_state values (?, ?)"),
                    ships.values(), (stmt, ship) -> {
                        try {
                            stmt.setString(1, ship.name());
                            stmt.setString(2, getShipState(ship.sailing()));
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into ship_state");

            insertData(rootConn.prepareStatement("insert into container values (?, ?)"),
                    containers, (stmt, container) -> {
                        try {
                            stmt.setString(1, container.code());
                            stmt.setString(2, getContainerType(container.type()));
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into container");

            insertData(rootConn.prepareStatement("insert into item_container values (?, ?)"),
                    item_container.entrySet(), (stmt, relation) -> {
                        try {
                            stmt.setString(1, relation.getKey());
                            stmt.setString(2, relation.getValue());
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into item_container");

            insertData(rootConn.prepareStatement("insert into container_ship values (?, ?)"),
                    container_ship.entrySet(), (stmt, relation) -> {
                        try {
                            stmt.setString(1, relation.getKey());
                            stmt.setString(2, relation.getValue());
                        } catch (SQLException e) {
                            throw new RuntimeException("Wrong parameter!", e);
                        }
                    });
            Debug.println("Loaded data into container_ship");

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to open file: staffs.csv", e);
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
        var user = verification.getCompanyManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getTaxRate(city, itemClass, CompanyManager.TaxType.Import) : -1;
    }

    @Override
    public double getExportTaxRate(LogInfo log, String city, String itemClass) {
        var user = verification.getCompanyManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getTaxRate(city, itemClass, CompanyManager.TaxType.Export) : -1;
    }

    @Override
    public boolean loadItemToContainer(LogInfo log, String itemName, String containerCode) {
        var user = verification.getCompanyManagers().getUser(verification.checkAuthority(log));
        return user != null && user.loadItemToContainer(itemName, containerCode);
    }

    @Override
    public boolean loadContainerToShip(LogInfo log, String shipName, String containerCode) {
        var user = verification.getCompanyManagers().getUser(verification.checkAuthority(log));
        return user != null && user.loadContainerToShip(shipName, containerCode);
    }

    @Override
    public boolean shipStartSailing(LogInfo log, String shipName) {
        var user = verification.getCompanyManagers().getUser(verification.checkAuthority(log));
        return user != null && user.shipStartSailing(shipName);
    }

    @Override
    public boolean unloadItem(LogInfo log, String itemName) {
        var user = verification.getCompanyManagers().getUser(verification.checkAuthority(log));
        return user != null && user.unloadItem(itemName);
    }

    @Override
    public boolean itemWaitForChecking(LogInfo log, String item) {
        var user = verification.getCompanyManagers().getUser(verification.checkAuthority(log));
        return user != null && user.itemWaitForChecking(item);
    }

    @Override
    public boolean newItem(LogInfo log, ItemInfo item) {
        var user = verification.getCouriers().getUser(verification.checkAuthority(log));
        return user != null && user.newItem(item);
    }

    @Override
    public boolean setItemState(LogInfo log, String itemName, ItemState state) {
        var user = verification.getCouriers().getUser(verification.checkAuthority(log));
        return user != null && user.setItemState(itemName, state);
    }

    @Override
    public String[] getAllItemsAtPort(LogInfo log) {
        var user = verification.getSeaportOfficers().getUser(verification.checkAuthority(log));
        return user != null ? user.getAllItemsAtPort() : null;
    }

    @Override
    public boolean setItemCheckState(LogInfo log, String itemName, boolean success) {
        var user = verification.getSeaportOfficers().getUser(verification.checkAuthority(log));
        return user != null && user.setItemCheckState(itemName, success);
    }

    @Override
    public int getCompanyCount(LogInfo log) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getCount(SustcManager.CountType.Company) : -1;
    }

    @Override
    public int getCityCount(LogInfo log) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getCount(SustcManager.CountType.City) : -1;
    }

    @Override
    public int getCourierCount(LogInfo log) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getCount(SustcManager.CountType.Courier) : -1;
    }

    @Override
    public int getShipCount(LogInfo log) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getCount(SustcManager.CountType.Ship) : -1;
    }

    @Override
    public ItemInfo getItemInfo(LogInfo log, String name) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getItemInfo(name) : null;
    }

    @Override
    public ShipInfo getShipInfo(LogInfo log, String name) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getShipInfo(name) : null;
    }

    @Override
    public ContainerInfo getContainerInfo(LogInfo log, String code) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getContainerInfo(code) : null;
    }

    @Override
    public StaffInfo getStaffInfo(LogInfo log, String name) {
        var user = verification.getSUSTCManagers().getUser(verification.checkAuthority(log));
        return user != null ? user.getStaffInfo(name) : null;
    }
}
