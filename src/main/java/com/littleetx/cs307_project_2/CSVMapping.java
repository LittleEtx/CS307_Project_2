package com.littleetx.cs307_project_2;

import main.interfaces.ContainerInfo;
import main.interfaces.ItemState;
import main.interfaces.LogInfo;

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

    public static final int ITEM_NAME = 0;
    public static final int ITEM_CLASS = 1;
    public static final int ITEM_PRICE = 2;
    public static final int RETRIEVAL_CITY = 3;
    public static final int RETRIEVAL_COURIER = 4;
    public static final int DELIVERY_CITY = 5;
    public static final int DELIVERY_COURIER = 6;
    public static final int EXPORT_CITY = 7;
    public static final int IMPORT_CITY = 8;
    public static final int EXPORT_TAX = 9;
    public static final int IMPORT_TAX = 10;
    public static final int EXPORT_OFFICER = 11;
    public static final int IMPORT_OFFICER = 12;
    public static final int CONTAINER_CODE = 13;
    public static final int CONTAINER_TYPE = 14;
    public static final int SHIP_NAME = 15;
    public static final int COMPANY_NAME = 16;
    public static final int ITEM_STATE = 17;

    public static ItemState getItemState(String state) {
        return switch (state) {
            case "Picking-up" -> ItemState.PickingUp;
            case "To-Export Transporting" -> ItemState.ToExportTransporting;
            case "Export Checking" -> ItemState.ExportChecking;
            case "Export Check Fail" -> ItemState.ExportCheckFailed;
            case "Packing to Container" -> ItemState.PackingToContainer;
            case "Waiting for Shipping" -> ItemState.WaitingForShipping;
            case "Shipping" -> ItemState.Shipping;
            case "Unpacking from Container" -> ItemState.UnpackingFromContainer;
            case "Import Checking" -> ItemState.ImportChecking;
            case "Import Check Fail" -> ItemState.ImportCheckFailed;
            case "From-Import Transporting" -> ItemState.FromImportTransporting;
            case "Delivering" -> ItemState.Delivering;
            case "Finish" -> ItemState.Finish;
            default -> throw new IllegalArgumentException("Unknown item state: " + state);
        };
    }

    public static ContainerInfo.Type getContainerType(String type) {
        return switch (type) {
            case "Open Top Container" -> ContainerInfo.Type.OpenTop;
            case "Flat Rack Container" -> ContainerInfo.Type.FlatRack;
            case "Reefer Container" -> ContainerInfo.Type.Reefer;
            case "ISO Tank Container" -> ContainerInfo.Type.ISOTank;
            case "Dry Container" -> ContainerInfo.Type.Dry;
            default -> throw new IllegalArgumentException("Unknown container type: " + type);
        };
    }

    public static boolean getGender(String gender) {
        return "female".equals(gender);
    }
}
