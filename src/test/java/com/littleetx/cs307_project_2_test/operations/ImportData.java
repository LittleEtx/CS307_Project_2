package com.littleetx.cs307_project_2_test.operations;

import com.littleetx.cs307_project_2.Debug;
import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import main.DatabaseManipulation;

import java.io.File;

import static com.littleetx.cs307_project_2.database.DatabaseLoginInfo.getLoginInfo;

public class ImportData {
    private static final boolean initDatabase = false;

    public static void main(String[] args) {
        Debug.isOn = true;
        DatabaseLoginInfo info = getLoginInfo();
        DatabaseManipulation db = new DatabaseManipulation(
                info.getDatabase(), info.username(), info.password(), initDatabase
        );

        db.$import(new File("records.csv"), new File("staffs.csv"));
        System.out.println("Successfully import data");
    }
}
