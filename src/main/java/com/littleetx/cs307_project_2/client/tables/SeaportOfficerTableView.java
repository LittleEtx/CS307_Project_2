package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;

import java.util.Map;
import java.util.Objects;

public class SeaportOfficerTableView extends ItemTableView {
    public static String IMPORT_TAX = "Import Tax";
    public static String EXPORT_TAX = "Export Tax";
    public static String HANDLE_TYPE = "Handle Type";
    public static String TAX = "Tax";

    public enum TableType {
        IMPORT, EXPORT, FINISHED
    }

    public SeaportOfficerTableView(TableType tableType, Map<String, TaxInfo.Value> taxInfo) {
        super();
        addItemBasicInfo();
        addRouteInfo();

        switch (tableType) {
            case IMPORT -> addColumn(IMPORT_TAX, itemInfo -> convertPrice(taxInfo
                    .get(itemInfo.$class()).import_rate * itemInfo.price()), true);
            case EXPORT -> {
                addColumn(EXPORT_TAX, itemInfo -> convertPrice(taxInfo
                        .get(itemInfo.$class()).export_rate * itemInfo.price()), true);
            }
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
                        return convertPrice(taxInfo
                                .get(info.$class()).import_rate * info.price());
                    } else if (Objects.equals(info.export().officer(),
                            GlobalManager_Client.getStaffInfo().basicInfo().name())) {
                        return convertPrice(taxInfo
                                .get(info.$class()).export_rate * info.price());
                    } else {
                        return "Unknown";
                    }
                }, true);
            }
        }
        hideColumns(RETRIEVAL_CITY, DELIVERY_CITY);
    }
}
