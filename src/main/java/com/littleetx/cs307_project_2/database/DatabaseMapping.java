package com.littleetx.cs307_project_2.database;

import main.interfaces.ContainerInfo;
import main.interfaces.ItemState;
import main.interfaces.LogInfo.StaffType;

public class DatabaseMapping {
    public static String getStaffAuthorityDatabaseStr(StaffType type) {
        return switch (type) {
            case CompanyManager -> "COMPANY_MANAGER";
            case SeaportOfficer -> "SEAPORT_OFFICER";
            case Courier -> "COURIER";
            case SustcManager -> "SUSTC_MANAGER";
        };
    }

    public static String getStaffAuthorityVisualStr(StaffType type) {
        return switch (type) {
            case CompanyManager -> "Company Manager";
            case SeaportOfficer -> "Seaport Officer";
            case Courier -> "Courier";
            case SustcManager -> "SUSTC Manager";
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

    public static String getStateDatabaseString(ItemState state) {
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

    public static String getStateVisualString(ItemState state) {
        return switch (state) {
            case PickingUp -> "Picking Up";
            case ToExportTransporting -> "To-Export Transporting";
            case ExportChecking -> "Export Checking";
            case ExportCheckFailed -> "Export Check Fail";
            case PackingToContainer -> "Packing to Container";
            case WaitingForShipping -> "Waiting for Shipping";
            case Shipping -> "Shipping";
            case UnpackingFromContainer -> "Unpacking from Container";
            case ImportChecking -> "Import Checking";
            case ImportCheckFailed -> "Import Check Fail";
            case FromImportTransporting -> "From-Import Transporting";
            case Delivering -> "Delivering";
            case Finish -> "Finish";
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


    public static String getContainerTypeDatabaseStr(ContainerInfo.Type type) {
        return switch (type) {
            case Dry -> "DRY";
            case FlatRack -> "FLAT_ROCK";
            case ISOTank -> "ISO_TANK";
            case OpenTop -> "OPEN_TOP";
            case Reefer -> "REEFER";
        };
    }

    public static String getContainerTypeVisualStr(ContainerInfo.Type type) {
        return switch (type) {
            case Dry -> "Dry";
            case FlatRack -> "Flat Rack";
            case ISOTank -> "ISO Tank";
            case OpenTop -> "Open Top";
            case Reefer -> "Reefer";
        };
    }

    public static ContainerInfo.Type getContainerType(String type) {
        return switch (type) {
            case "DRY" -> ContainerInfo.Type.Dry;
            case "FLAT_ROCK" -> ContainerInfo.Type.FlatRack;
            case "ISO_TANK" -> ContainerInfo.Type.ISOTank;
            case "OPEN_TOP" -> ContainerInfo.Type.OpenTop;
            case "REEFER" -> ContainerInfo.Type.Reefer;
            default -> throw new IllegalArgumentException("Unknown type!");
        };
    }

    public static boolean getGender(String gender) {
        return "FEMALE".equals(gender);
    }

    public static String getGender(boolean isFemale) {
        return isFemale ? "FEMALE" : "MALE";
    }

    public static boolean getIsSailing(String state) {
        return "SAILING".equals(state);
    }

    public static boolean getIsContainerUsing(String state) {
        return "USING".equals(state);
    }
}
