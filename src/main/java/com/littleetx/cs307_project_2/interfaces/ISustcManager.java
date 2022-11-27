package cs307.project2.interfaces;

public interface ISustcManager {
	int getCompanyCount(LogInfo log);
	
	int getCityCount(LogInfo log);
	
	int getCourierCount(LogInfo log);

	int getShipCount(LogInfo log);

	ItemInfo getItemInfo(LogInfo log, String name);

	ShipInfo getShipInfo(LogInfo log, String name);

	ContainerInfo getContainerInfo(LogInfo log, String code);

	StaffInfo getStaffInfo(LogInfo log, String name);
}
