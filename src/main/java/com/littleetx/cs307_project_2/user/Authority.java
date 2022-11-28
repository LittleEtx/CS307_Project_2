package com.littleetx.cs307_project_2.user;

import java.sql.Connection;

public class Authority {
    private Connection conn;
    public Authority(Connection conn) {
        this.conn = conn;
    }
    public boolean checkAuthority(String username, String password) {
        return true;
    }
}
