package com.littleetx.cs307_project_2.user;

import com.littleetx.cs307_project_2.DatabaseMapping;
import com.littleetx.cs307_project_2.GlobalQuery;
import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import static cs307.project2.interfaces.LogInfo.StaffType;

public class Verification <T extends User> {
    private final Connection conn;
    private final Class<T> userClass;
    private final StaffType staffType;
    public Verification(Connection conn, Class<T> userClass, StaffType staffType) {
        this.conn = conn;
        this.userClass = userClass;
        this.staffType = staffType;
    }

    public StaffInfo checkAuthority(LogInfo info) {
        if (staffType != info.type())
            return null;

        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM staff join verification on staff.id = verification.staff_id " +
                            "left join staff_company on staff.id = staff_company.staff_id " +
                            "left join staff_city on staff.id = staff_city.staff_id " +
                            "WHERE name = ? AND password = ?");
            stmt.setString(1, info.name());
            stmt.setString(2, info.password());
            var result = stmt.executeQuery();
            if (!result.next() || DatabaseMapping.getStaffAuthority(
                    result.getString("authority")) != staffType) {
                return null;
            }
            return new StaffInfo(
                    info,
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

    public T getUser(LogInfo info) {
        StaffInfo staffInfo = checkAuthority(info);
        if (staffInfo == null) {
            return null;
        }

        T user;
        try {
            user = userClass.getConstructor(Connection.class, StaffInfo.class).newInstance(conn, staffInfo);
        } catch (NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create user", e);
        }
        //authorization is not of the type
        if (user.getStaffType() != info.type()) {
            return null;
        }
        return user;
    }
}
