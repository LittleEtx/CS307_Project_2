package cs307.project2.interfaces;

public interface ISeaportOfficer {
	String[] getAllItemsAtPort(LogInfo log);
	
	boolean setItemCheckState(LogInfo log, String itemName, boolean success);
}
