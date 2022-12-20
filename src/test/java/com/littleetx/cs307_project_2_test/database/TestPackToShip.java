package com.littleetx.cs307_project_2_test.database;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.CompanyManager;

public class TestPackToShip {
    public static void main(String[] args) {
        CompanyManager manager = (new Verification(DatabaseLoginInfo.getLoginInfo()))
                .getUser(10203261, CompanyManager.class);

        manager.loadItemToContainer("banana-136c", "c8458f8d");

        System.out.println(manager.loadContainerToShip("f1d76a3a", "632b8f09")); //false
        System.out.println(manager.loadContainerToShip("a76061bc", "c8458f8d")); //false
        System.out.println(manager.loadContainerToShip("1bdb0f2e ", "c8458f8d")); //false
    }
}
