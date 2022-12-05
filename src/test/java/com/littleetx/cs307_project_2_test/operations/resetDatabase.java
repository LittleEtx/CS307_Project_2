package com.littleetx.cs307_project_2_test.operations;

import com.littleetx.cs307_project_2.file_reader.SQLReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.littleetx.cs307_project_2_test.GetLoginInfo.*;

public class resetDatabase {
    public static void main(String[] args) {
        LoginInfo info = getLoginInfo();
        Connection con;
        try {
            con = DriverManager.getConnection(getUrl(info, true), info.username(), info.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SQLReader.runSQL("scripts\\DropTables.sql", con);
        SQLReader.runSQL("scripts\\DDL.sql", con);
        System.out.println("Database reset successfully");
    }
}
