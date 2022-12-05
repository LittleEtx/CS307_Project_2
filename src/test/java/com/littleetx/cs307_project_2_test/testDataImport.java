package com.littleetx.cs307_project_2_test;

import com.littleetx.cs307_project_2.DatabaseManipulation;

public class testDataImport {
    public static void main(String[] args) {
        GetLoginInfo.LoginInfo info = GetLoginInfo.getLoginInfo();
        DatabaseManipulation db = new DatabaseManipulation(
            GetLoginInfo.getUrl(info),info.username(),info.password(), true
        );

        db.$import("records.csv", "staffs.csv");
        System.out.println("Successfully import data");
    }
}
