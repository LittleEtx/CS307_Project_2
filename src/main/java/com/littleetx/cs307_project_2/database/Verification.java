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
import java.time.LocalDate;

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

    public StaffInfo checkAuthority(String username, String password) {
        Connection conn = GlobalQuery.getRootConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM staff join verification on staff.id = verification.staff_id " +
                            "left join staff_company on staff.id = staff_company.staff_id " +
                            "left join staff_city on staff.id = staff_city.staff_id " +
                            "WHERE name = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            var result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            return new StaffInfo(
                    new LogInfo(
                            username,
                            DatabaseMapping.getStaffAuthority(
                                    result.getString("authority")),
                            password
                    ),
                    GlobalQuery.getCompanyName(result.getInt("company_id")),
                    GlobalQuery.getCityName(result.getInt("city_id")),
                    DatabaseMapping.getGender(result.getString("gender")),
                    result.getDate("birth").toLocalDate()
                            .until(LocalDate.now()).getYears(),
                    result.getString("phone_number")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify", e);
        }
    }

    public StaffInfo checkAuthority(LogInfo logInfo) {
        StaffInfo info = checkAuthority(logInfo.name(), logInfo.password());
        if (info == null || info.basicInfo().type() != logInfo.type()) {
            return null;
        }
        return info;
    }
}
