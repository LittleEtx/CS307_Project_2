package com.littleetx.cs307_project_2;

import cs307.project2.interfaces.LogInfo;

public class CSVMapping {
    public static final int STAFF_NAME = 0;
    public static final int STAFF_TYPE = 1;

    public static final int STAFF_COMPANY = 2;
    public static final int STAFF_CITY = 3;
    public static final int STAFF_GENDER = 4;
    public static final int STAFF_AGE = 5;
    public static final int STAFF_PHONE = 6;
    public static final int STAFF_PASSWORD = 7;

    public static LogInfo.StaffType getStaffType(String type) {
        return switch (type) {
            case "Company Manager" -> LogInfo.StaffType.CompanyManager;
            case "Seaport Officer" -> LogInfo.StaffType.SeaportOfficer;
            case "Courier" -> LogInfo.StaffType.Courier;
            case "SUSTC Department Manager" -> LogInfo.StaffType.SustcManager;
            default -> throw new IllegalArgumentException("Unknown staff type: " + type);
        };
    }

    public static String getStaffAuthority(LogInfo.StaffType type) {
        return switch (type) {
            case CompanyManager -> "COMPANY_MANAGER";
            case SeaportOfficer -> "SEAPORT_OFFICER";
            case Courier -> "COURIER";
            case SustcManager -> "SUSTC_MANAGER";
        };
    }
}
