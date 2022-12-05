package com.littleetx.cs307_project_2_test;

import com.littleetx.cs307_project_2.GlobalQuery;
import com.littleetx.cs307_project_2.user.Courier;
import com.littleetx.cs307_project_2.user.Verification;
import cs307.project2.interfaces.LogInfo;

import java.sql.Connection;

public class testVerification {
    public static void main(String[] args) {
        Connection conn = GlobalQuery.getRootConnection();
        Verification<Courier> courier_ver = new Verification<>(
                conn, Courier.class, LogInfo.StaffType.Courier
        );
        System.out.println(courier_ver.checkAuthority(
                new LogInfo("Zou Zhen", LogInfo.StaffType.Courier, "8632254645302722788")
        ));
        System.out.println(courier_ver.checkAuthority(
                new LogInfo("Zou Zhen", LogInfo.StaffType.Courier, "114514")
        ));
        System.out.println(courier_ver.checkAuthority(
                new LogInfo("Zou Zhen", LogInfo.StaffType.SeaportOfficer, "8632254645302722788")
        ));
        System.out.println(courier_ver.checkAuthority(
                new LogInfo("Jing Xian", LogInfo.StaffType.Courier, "4378861802694094505")
        ));
        System.out.println(courier_ver.checkAuthority(
                new LogInfo("Jing Xian", LogInfo.StaffType.SeaportOfficer, "4378861802694094505")
        ));
    }
}
