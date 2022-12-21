package com.littleetx.cs307_project_2.database;

import com.littleetx.cs307_project_2.database.user.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class UserGetter<T extends User> {
    private final Connection conn;
    private final Class<T> userClass;

    public UserGetter(Connection conn, Class<T> userClass) {
        this.conn = conn;
        this.userClass = userClass;
    }

    /**
     * @param id must be a valid id for the type
     */
    public T getUser(int id) {
        if (id < 0) {
            return null;
        }
        T user;
        try {
            user = this.userClass.getConstructor(Connection.class, Integer.class).newInstance(conn, id);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create user", e);
        } catch (InvocationTargetException e) {
            return null;
        }
        return user;
    }
}
