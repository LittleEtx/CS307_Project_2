package com.littleetx.cs307_project_2_test.operations;

import com.littleetx.cs307_project_2.file_reader.SQLReader;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.littleetx.cs307_project_2_test.GetLoginInfo.*;

public class resetDatabase {
    public static void main(String[] args) {
        LoginInfo info = getLoginInfo();
        Connection con;
        try {
            con = DriverManager.getConnection(getUrl(info), info.username(), info.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (Statement stmt = con.createStatement()) {
            File drop = new File("scripts\\DropTables.sql");
            SQLReader reader = new SQLReader(drop);
            for (String sql : reader) {
                stmt.execute(sql);
            }
            reader.close();

            File create = new File("scripts\\DDL.sql");
            reader = new SQLReader(create);
            for (String sql : reader) {
                stmt.execute(sql);
            }
            reader.close();

        } catch (Exception e) {
            System.err.println("Database reset failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Database reset successfully");
    }
}
