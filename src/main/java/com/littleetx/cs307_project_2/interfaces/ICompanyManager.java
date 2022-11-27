package cs307.project2.interfaces;

public interface ICompanyManager {
	double getImportTaxRate(LogInfo log, String city, String itemClass);
	
	double getExportTaxRate(LogInfo log, String city, String itemClass);

	boolean loadItemToContainer(LogInfo log, String itemName, String containerCode);

	boolean loadContainerToShip(LogInfo log, String shipName, String containerCode);

	boolean shipStartSailing(LogInfo log, String shipName);

	boolean unloadItem(LogInfo log, String itemName);

	boolean itemWaitForChecking(LogInfo log, String item);
}
