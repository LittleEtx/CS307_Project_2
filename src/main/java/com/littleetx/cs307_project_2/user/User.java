package com.littleetx.cs307_project_2.user;

import cs307.project2.interfaces.LogInfo;

import java.sql.*;

abstract public class User {
    protected Connection conn;
    protected LogInfo logInfo;
    protected User(Connection conn, LogInfo info) {
        this.conn = conn;
        this.logInfo = info;
    }


}
