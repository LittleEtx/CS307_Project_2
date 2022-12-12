package com.littleetx.cs307_project_2_test;

import com.littleetx.cs307_project_2.DatabaseManipulation;
import com.littleetx.cs307_project_2.Debug;
import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;

import static com.littleetx.cs307_project_2.database.DatabaseLoginInfo.getLoginInfo;

public class testDataImport {
    private static final boolean initDatabase = false;
    public static void main(String[] args) {
        Debug.isOn = true;
        DatabaseLoginInfo info = getLoginInfo();
        DatabaseManipulation db = new DatabaseManipulation(
                info.getUrl(!initDatabase), info.username(), info.password(), initDatabase
        );

        db.$import("records.csv", "staffs.csv");
        System.out.println("Successfully import data");
    }
}
