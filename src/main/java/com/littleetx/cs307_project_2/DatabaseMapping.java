package com.littleetx.cs307_project_2;

import cs307.project2.interfaces.ContainerInfo;
import cs307.project2.interfaces.ItemState;
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

    public static String getItemState(ItemState state) {
        return switch (state) {
            case PickingUp -> "PICKING_UP";
            case ToExportTransporting -> "TO_EXPORT_TRANSPORTING";
            case ExportChecking -> "EXPORT_CHECKING";
            case ExportCheckFailed -> "EXPORT_CHECK_FAILED";
            case PackingToContainer -> "PACKING_TO_CONTAINER";
            case WaitingForShipping -> "WAITING_FOR_SHIPPING";
            case Shipping -> "SHIPPING";
            case UnpackingFromContainer -> "UNPACKING_FROM_CONTAINER";
            case ImportChecking -> "IMPORT_CHECKING";
            case ImportCheckFailed -> "IMPORT_CHECK_FAILED";
            case FromImportTransporting -> "FROM_IMPORT_TRANSPORTING";
            case Delivering -> "DELIVERING";
            case Finish -> "FINISH";
        };
    }

    public static String getShipState(boolean sailing) {
        return sailing ? "SAILING" : "DOCKED";
    }
    public static String getContainerType(ContainerInfo.Type type) {
        return switch (type) {
            case Dry -> "DRY";
            case FlatRack -> "FLAT_ROCK";
            case ISOTank -> "ISO_TANK";
            case OpenTop -> "OPEN_TOP";
            case Reefer -> "REEFER";
        };
    }
}
