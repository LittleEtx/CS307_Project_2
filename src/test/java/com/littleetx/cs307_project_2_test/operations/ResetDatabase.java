package com.littleetx.cs307_project_2_test.operations;

import com.littleetx.cs307_project_2.database.GlobalQuery;
import com.littleetx.cs307_project_2.file_reader.SQLReader;

import java.sql.Connection;

public class ResetDatabase {
    public static void main(String[] args) {
        Connection con = GlobalQuery.getRootConnection();
        SQLReader.runSQL("scripts\\DropTables.sql", con);
        SQLReader.runSQL("scripts\\DDL.sql", con);
        SQLReader.runSQL("scripts\\Views.sql", con);
        SQLReader.runSQL("scripts\\GrantUserRights.sql", con);
        System.out.println("Database reset successfully");
    }
}
