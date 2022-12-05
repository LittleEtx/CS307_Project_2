package com.littleetx.cs307_project_2_test;

import com.littleetx.cs307_project_2.DatabaseManipulation;
import com.littleetx.cs307_project_2.Debug;

public class testDataImport {
    private static final boolean initDatabase = false;
    public static void main(String[] args) {
        Debug.isOn = true;
        GetLoginInfo.LoginInfo info = GetLoginInfo.getLoginInfo();
        DatabaseManipulation db = new DatabaseManipulation(
            GetLoginInfo.getUrl(info, !initDatabase),info.username(),info.password(), initDatabase
        );

        db.$import("records.csv", "staffs.csv");
        System.out.println("Successfully import data");
    }
}
