package com.littleetx.cs307_project_2_test.database;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.Courier;
import main.interfaces.ItemInfo;

import java.util.Map;


public class TestCourier {
    public static void main(String[] args) {
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        Courier courier = verification.getUser(
                verification.checkAuthority("11202206", "3621471600376504500"), Courier.class);
        Map<String, ItemInfo> items = courier.getAllItems(Courier.GetItemType.OnGoing);
        for (var entry : items.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


}
