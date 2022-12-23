package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;

import java.util.Objects;

public class SeaportOfficerItemTableView extends ItemTableView {
    public static String HANDLE_TYPE = "Handle Type";
    public static String TAX = "Tax";

    public enum TableType {
        IMPORT, EXPORT, FINISHED
    }

    public SeaportOfficerItemTableView(TableType tableType) {
        super();
        addItemBasicInfo();
        addRouteInfo();

        switch (tableType) {
            case IMPORT -> addColumn(IMPORT_TAX, itemInfo ->
                    convertPrice(itemInfo.$import().tax()), true);
            case EXPORT -> addColumn(EXPORT_TAX, itemInfo ->
                    convertPrice(itemInfo.export().tax()), true);
            case FINISHED -> {
                addColumn(HANDLE_TYPE, info -> {
                    if (Objects.equals(info.$import().officer(),
                            GlobalManager_Client.getStaffInfo().basicInfo().name())) {
                        return "Import";
                    } else if (Objects.equals(info.export().officer(),
                            GlobalManager_Client.getStaffInfo().basicInfo().name())) {
                        return "Export";
                    } else {
                        return "Unknown";
                    }
                });
                addColumn(TAX, info -> {
                    if (Objects.equals(info.$import().officer(),
                            GlobalManager_Client.getStaffInfo().basicInfo().name())) {
                        return convertPrice(info.$import().tax());
                    } else if (Objects.equals(info.export().officer(),
                            GlobalManager_Client.getStaffInfo().basicInfo().name())) {
                        return convertPrice(info.export().tax());
                    } else {
                        return "Unknown";
                    }
                }, true);
            }
        }
        hideColumns(RETRIEVAL_CITY, DELIVERY_CITY);
    }
}
