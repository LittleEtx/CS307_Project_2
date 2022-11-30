package com.littleetx.cs307_project_2.user;

import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;

import java.sql.Connection;

abstract public class User {
    /**
     * connection to the database
     */
    protected Connection conn;
    /**
     * information of the current staff
     */
    protected StaffInfo staffInfo;
    protected User(Connection conn, StaffInfo info) {
        this.conn = conn;
        this.staffInfo = info;
    }
    abstract public LogInfo.StaffType getStaffType();

    public boolean changePassword(String newPassword) {
        //TODO: (optional) change password
        return false;
    }

    public boolean changePhoneNumber(String newPhoneNumber) {
        //TODO: (optional) change phone number
        return false;
    }
}
