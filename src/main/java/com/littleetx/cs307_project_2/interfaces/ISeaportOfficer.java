package com.littleetx.cs307_project_2.interfaces;

public interface ISeaportOfficer {
	String[] getAllItemsAtPort(LogInfo log);
	
	boolean setItemCheckState(LogInfo log, String itemName, boolean success);
}
