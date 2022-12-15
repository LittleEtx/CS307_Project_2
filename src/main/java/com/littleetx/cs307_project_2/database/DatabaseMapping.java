package com.littleetx.cs307_project_2.database;

import cs307.project2.interfaces.ContainerInfo;
import cs307.project2.interfaces.ItemState;

import static cs307.project2.interfaces.LogInfo.StaffType;

public class DatabaseMapping {
    public static String getStaffAuthority(StaffType type) {
        return switch (type) {
            case CompanyManager -> "COMPANY_MANAGER";
            case SeaportOfficer -> "SEAPORT_OFFICER";
            case Courier -> "COURIER";
            case SustcManager -> "SUSTC_MANAGER";
        };
    }

    public static StaffType getStaffAuthority(String type) {
        return switch (type) {
            case "COMPANY_MANAGER" -> StaffType.CompanyManager;
            case "SEAPORT_OFFICER" -> StaffType.SeaportOfficer;
            case "COURIER" -> StaffType.Courier;
            case "SUSTC_MANAGER" -> StaffType.SustcManager;
            default -> throw new IllegalArgumentException("Unknown type!");
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

    public static ItemState getItemState(String state) {
        return switch (state) {
            case "PICKING_UP" -> ItemState.PickingUp;
            case "TO_EXPORT_TRANSPORTING" -> ItemState.ToExportTransporting;
            case "EXPORT_CHECKING" -> ItemState.ExportChecking;
            case "EXPORT_CHECK_FAILED" -> ItemState.ExportCheckFailed;
            case "PACKING_TO_CONTAINER" -> ItemState.PackingToContainer;
            case "WAITING_FOR_SHIPPING" -> ItemState.WaitingForShipping;
            case "SHIPPING" -> ItemState.Shipping;
            case "UNPACKING_FROM_CONTAINER" -> ItemState.UnpackingFromContainer;
            case "IMPORT_CHECKING" -> ItemState.ImportChecking;
            case "IMPORT_CHECK_FAILED" -> ItemState.ImportCheckFailed;
            case "FROM_IMPORT_TRANSPORTING" -> ItemState.FromImportTransporting;
            case "DELIVERING" -> ItemState.Delivering;
            case "FINISH" -> ItemState.Finish;
            default -> throw new IllegalArgumentException("Unknown state!");
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

    public static boolean getGender(String gender) {
        return "FEMALE".equals(gender);
    }

    public static String getGender(boolean isFemale) {
        return isFemale ? "FEMALE" : "MALE";
    }
}
