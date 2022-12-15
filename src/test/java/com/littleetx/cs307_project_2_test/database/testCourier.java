package com.littleetx.cs307_project_2_test.database;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.Courier;
import cs307.project2.interfaces.ItemInfo;

import java.util.Map;


public class testCourier {
    public static void main(String[] args) {
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        Courier courier = verification.getCouriers().getUser(
                verification.checkAuthority("11202206", "3621471600376504500"));
        Map<String, ItemInfo> items = courier.getAllItems(Courier.GetItemType.OnGoing);
        for (var entry : items.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


}
