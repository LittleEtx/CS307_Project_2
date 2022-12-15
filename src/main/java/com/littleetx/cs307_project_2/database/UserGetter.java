package com.littleetx.cs307_project_2.database;

import com.littleetx.cs307_project_2.database.user.User;
import cs307.project2.interfaces.StaffInfo;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class UserGetter<T extends User> {
    private final Connection conn;
    private final Class<T> userClass;

    public UserGetter(Connection conn, Class<T> userClass) {
        this.conn = conn;
        this.userClass = userClass;
    }

    public T getUser(int id) {
        if (id < 0) {
            return null;
        }
        StaffInfo info = GlobalQuery.getStaffInfo(id);
        T user;
        try {
            user = userClass.getConstructor(Connection.class, Integer.class, StaffInfo.class).newInstance(conn, id, info);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create user", e);
        } catch (InvocationTargetException e) {
            return null;
        }
        return user;
    }
}
