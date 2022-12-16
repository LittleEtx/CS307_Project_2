package test.answers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class CompanyManagerUserTest implements Serializable {

    public HashMap<List<Object>, Double> getImportTaxRate = new HashMap<>();

    public HashMap<List<Object>, Double> getExportTaxRate = new HashMap<>();

    public HashMap<List<Object>, Boolean> loadItemToContainer = new HashMap<>();

    public HashMap<List<Object>, Boolean> loadContainerToShip = new HashMap<>();

    public HashMap<List<Object>, Boolean> shipStartSailing = new HashMap<>();

    public HashMap<List<Object>, Boolean> unloadItem = new HashMap<>();

    public HashMap<List<Object>, Boolean> itemWaitForChecking = new HashMap<>();

}
