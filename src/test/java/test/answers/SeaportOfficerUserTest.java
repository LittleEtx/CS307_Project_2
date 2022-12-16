package test.answers;

import main.interfaces.LogInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class SeaportOfficerUserTest implements Serializable {

    public HashMap<LogInfo, String[]> getAllItemsAtPort = new HashMap<>();

    public HashMap<List<Object>, Boolean> setItemCheckState = new HashMap<>();

}
