package com.littleetx.cs307_project_2_test.operations;

import com.littleetx.cs307_project_2.file_reader.SQLReader;
import com.littleetx.cs307_project_2_test.GetLoginInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class deleteSUSTC {
    public static void main(String[] args) {
        GetLoginInfo.LoginInfo info = GetLoginInfo.getLoginInfo();
        Connection con;
        try {
            con = DriverManager.getConnection(GetLoginInfo.getUrl(info, false),
                    info.username(), info.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            con.prepareStatement("DROP DATABASE sustc").execute();
        } catch (SQLException e) {
            throw new RuntimeException("failed to ", e);
        }
        SQLReader.runSQL("scripts\\DropUsers.sql", con);
        System.out.println("Database deleted successfully");
    }
}
