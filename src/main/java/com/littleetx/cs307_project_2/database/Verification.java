package com.littleetx.cs307_project_2.database;

import com.littleetx.cs307_project_2.database.user.*;
import main.interfaces.LogInfo;
import main.interfaces.StaffInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Verification {
    private final UserGetter<CompanyManager> companyManagers;
    private final UserGetter<SeaportOfficer> seaportOfficers;
    private final UserGetter<Courier> couriers;
    private final UserGetter<SUSTCManager> sustcManagers;

    public Verification(DatabaseLoginInfo loginInfo) {
        this(loginInfo.getUrl());
    }

    public Verification(String url) {
        try {
            companyManagers = new UserGetter<>(DriverManager.getConnection(url,
                    "company_manager", "company_manager"), CompanyManager.class);
            seaportOfficers = new UserGetter<>(DriverManager.getConnection(url,
                    "seaport_officer", "seaport_officer"), SeaportOfficer.class);
            couriers = new UserGetter<>(DriverManager.getConnection(url,
                    "courier", "courier"), Courier.class);
            sustcManagers = new UserGetter<>(DriverManager.getConnection(url,
                    "sustc_manager", "sustc_manager"), SUSTCManager.class);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get users", e);
        }
    }

    public int checkAuthority(String user, String password) {
        Connection conn = GlobalQuery.getRootConnection();
        try {
            PreparedStatement stmt;
            try {
                //using id
                int id = Integer.parseInt(user);
                stmt = conn.prepareStatement(
                        "SELECT * FROM staff join verification on " +
                                "staff.id = verification.staff_id " +
                                "WHERE staff.id = ? AND verification.password = ?");
                stmt.setInt(1, id);
            } catch (NumberFormatException e) {
                //using name
                stmt = conn.prepareStatement(
                        "SELECT * FROM staff join verification " +
                                "on staff.id = verification.staff_id " +
                                "WHERE name = ? AND password = ?");
                stmt.setString(1, user);
            }
            stmt.setString(2, password);
            var result = stmt.executeQuery();
            if (result.next()) {
                return result.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check authority", e);
        }
    }

    public int checkAuthority(LogInfo logInfo) {
        int id = checkAuthority(logInfo.name(), logInfo.password());
        if (id < 0) {
            return id;
        }
        StaffInfo info = GlobalQuery.getStaffInfo(id);
        assert info != null;
        if (info.basicInfo().type() != logInfo.type()) {
            return -1;
        }
        return id;
    }

    public <T extends User> T getUser(LogInfo logInfo, Class<T> userClass) {
        return getUser(checkAuthority(logInfo), userClass);
    }

    public <T extends User> T getUser(int id, Class<T> userClass) {
        if (id < 0) {
            return null;
        }
        StaffInfo info = GlobalQuery.getStaffInfo(id);
        assert info != null;
        switch (info.basicInfo().type()) {
            case SustcManager -> {
                return (T) sustcManagers.getUser(id, userClass);
            }
            case CompanyManager -> {
                return (T) companyManagers.getUser(id, userClass);
            }
            case SeaportOfficer -> {
                return (T) seaportOfficers.getUser(id, userClass);
            }
            case Courier -> {
                return (T) couriers.getUser(id, userClass);
            }
            default -> {
                return null;
            }
        }
    }
}
