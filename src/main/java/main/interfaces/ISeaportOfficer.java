package main.interfaces;

public interface ISeaportOfficer {
    String[] getAllItemsAtPort(LogInfo log);

    boolean setItemCheckState(LogInfo log, String itemName, boolean success);
}
