package com.littleetx.cs307_project_2_test;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import cs307.project2.interfaces.LogInfo;

public class testVerification {
    public static void main(String[] args) {
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        System.out.println(verification.getCouriers().getUser(
                verification.checkAuthority(
                        new LogInfo("Zou Zhen", LogInfo.StaffType.Courier, "8632254645302722788"))));
        System.out.println(verification.getCouriers().getUser(
                verification.checkAuthority(
                        new LogInfo("Zou Zhen", LogInfo.StaffType.Courier, "114514"))));
        System.out.println(verification.getCouriers().getUser(
                verification.checkAuthority(
                        new LogInfo("Zou Zhen", LogInfo.StaffType.SeaportOfficer, "8632254645302722788"))));
        System.out.println(verification.getCouriers().getUser(
                verification.checkAuthority(
                        new LogInfo("Jing Xian", LogInfo.StaffType.Courier, "4378861802694094505"))));
        System.out.println(verification.getCouriers().getUser(
                verification.checkAuthority(
                        new LogInfo("Jing Xian", LogInfo.StaffType.SeaportOfficer, "4378861802694094505"))));
    }
}
