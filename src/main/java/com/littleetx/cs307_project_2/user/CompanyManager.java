package com.littleetx.cs307_project_2.user;

import cs307.project2.interfaces.LogInfo;
import java.sql.Connection;

public class CompanyManager extends User {
    CompanyManager(Connection conn, LogInfo info) {
        super(conn, info);
    }

    public double getImportTaxRate(String city, String itemClass) {
        return 0;
    }

    public double getExportTaxRate(String city, String itemClass) {
        return 0;
    }

    public boolean loadItemToContainer(String itemName, String containerCode) {
        return false;
    }

    public boolean loadContainerToShip(String shipName, String containerCode) {
        return false;
    }

    public boolean shipStartSailing(String shipName) {
        return false;
    }

    public boolean unloadItem(LogInfo logInfo, String s) {
        return false;
    }

    public boolean itemWaitForChecking(LogInfo logInfo, String s) {
        return false;
    }
}
