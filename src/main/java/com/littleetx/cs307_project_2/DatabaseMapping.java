package com.littleetx.cs307_project_2;

import cs307.project2.interfaces.LogInfo;

public class DatabaseMapping {
    public static String getStaffAuthority(LogInfo.StaffType type) {
        return switch (type) {
            case CompanyManager -> "COMPANY_MANAGER";
            case SeaportOfficer -> "SEAPORT_OFFICER";
            case Courier -> "COURIER";
            case SustcManager -> "SUSTC_MANAGER";
        };
    }
}
