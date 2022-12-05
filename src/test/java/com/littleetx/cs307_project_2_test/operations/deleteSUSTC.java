package com.littleetx.cs307_project_2_test.operations;

import com.littleetx.cs307_project_2.GlobalQuery;
import com.littleetx.cs307_project_2.file_reader.SQLReader;

import java.sql.Connection;
import java.sql.SQLException;

public class deleteSUSTC {
    public static void main(String[] args) {
        Connection conn = GlobalQuery.getRootConnection();
        try {
            conn.prepareStatement("DROP DATABASE sustc").execute();
        } catch (SQLException e) {
            throw new RuntimeException("failed to ", e);
        }
        SQLReader.runSQL("scripts\\DropUsers.sql", conn);
        System.out.println("Database deleted successfully");
    }
}
