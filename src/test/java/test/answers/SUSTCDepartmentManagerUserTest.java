package test.answers;

import main.interfaces.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class SUSTCDepartmentManagerUserTest implements Serializable {

    public HashMap<LogInfo, Integer> getCompanyCount = new HashMap<>();

    public HashMap<LogInfo, Integer> getCityCount = new HashMap<>();

    public HashMap<LogInfo, Integer> getCourierCount = new HashMap<>();

    public HashMap<LogInfo, Integer> getShipCount = new HashMap<>();

    public HashMap<List<Object>, ItemInfo> getItemInfo = new HashMap<>();

    public HashMap<List<Object>, ShipInfo> getShipInfo = new HashMap<>();

    public HashMap<List<Object>, ContainerInfo> getContainerInfo = new HashMap<>();

    public HashMap<List<Object>, StaffInfo> getStaffInfo = new HashMap<>();


}
