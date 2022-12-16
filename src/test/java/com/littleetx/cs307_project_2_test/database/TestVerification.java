package com.littleetx.cs307_project_2_test.database;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.database.user.SeaportOfficer;
import main.interfaces.LogInfo;

public class TestVerification {
    public static void main(String[] args) {
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        System.out.println(verification.getUser(new LogInfo("Zou Zhen",
                LogInfo.StaffType.Courier, "8632254645302722788"), Courier.class));
        System.out.println(verification.getUser(new LogInfo("Zou Zhen",
                LogInfo.StaffType.Courier, "8632254645302722788"), SeaportOfficer.class));
        System.out.println(verification.getUser(new LogInfo("Zou Zhen",
                LogInfo.StaffType.Courier, "114514"), Courier.class));
        System.out.println(verification.getUser(new LogInfo("Zou Zhen",
                LogInfo.StaffType.SeaportOfficer, "8632254645302722788"), Courier.class));
        System.out.println(verification.getUser(new LogInfo("Jing Xian",
                LogInfo.StaffType.Courier, "4378861802694094505"), Courier.class));
        System.out.println(verification.getUser(new LogInfo("Jing Xian",
                LogInfo.StaffType.SeaportOfficer, "4378861802694094505"), Courier.class));
    }
}
