package com.littleetx.cs307_project_2_test.database;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.Courier;

public class TestNewItem {
    public static void main(String[] args) {
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        System.out.println(verification.getUser(10102899, Courier.class)
                .newItem("newItem1", "apple", 999, 1, 2, 8));
    }
}
