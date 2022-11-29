package com.littleetx.cs307_project_2.user;

import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class Verification <T extends User> {
    private final Connection conn;
    private final Class<T> userClass;
    public Verification(Connection conn, Class<T> userClass) {
        this.conn = conn;
        this.userClass = userClass;
    }

    public StaffInfo checkAuthority(LogInfo info) {
        //TODO: query database for verification
        return null;
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
