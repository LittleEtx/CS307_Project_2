package com.littleetx.cs307_project_2.database;

import com.littleetx.cs307_project_2.database.user.CompanyManager;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.database.user.SeaportOfficer;
import com.littleetx.cs307_project_2.database.user.SustcManager;
import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Verification {
    private final UserGetter<CompanyManager> companyManagers;
    private final UserGetter<SeaportOfficer> seaportOfficers;
    private final UserGetter<Courier> couriers;
    private final UserGetter<SustcManager> sustcManagers;

    public UserGetter<CompanyManager> getCompanyManagers() {
        return companyManagers;
    }

    public UserGetter<SeaportOfficer> getSeaportOfficers() {
        return seaportOfficers;
    }

    public UserGetter<Courier> getCouriers() {
        return couriers;
    }

    public UserGetter<SustcManager> getSUSTCManagers() {
        return sustcManagers;
    }

    public Verification(DatabaseLoginInfo loginInfo) {
        this(loginInfo.getUrl(true));
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
                    "sustc_manager", "sustc_manager"), SustcManager.class);
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
                        "SELECT * FROM staff natural join verification WHERE id = ? AND password = ?");
                stmt.setInt(1, id);
            } catch (NumberFormatException e) {
                //using name
                stmt = conn.prepareStatement(
                        "SELECT * FROM staff natural join verification WHERE name = ? AND password = ?");
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

    public StaffInfo checkAuthority(LogInfo logInfo) {
        int id = checkAuthority(logInfo.name(), logInfo.password());
        if (id < 0) {
            return null;
        }
        StaffInfo info = GlobalQuery.getStaffInfo(id);
        assert info != null;
        if (info.basicInfo().type() != logInfo.type()) {
            return null;
        }
        return info;
    }
}
