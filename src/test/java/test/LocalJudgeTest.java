package test;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import com.littleetx.cs307_project_2.file_reader.SQLReader;
import main.interfaces.*;
import org.junit.jupiter.api.*;
import test.answers.CompanyManagerUserTest;
import test.answers.CourierUserTest;
import test.answers.SUSTCDepartmentManagerUserTest;
import test.answers.SeaportOfficerUserTest;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocalJudgeTest {

    private static final String database;

    private static final String root;

    private static final String pass;
    private static final String manipulationImplClassName = "main.DatabaseManipulation";

    static {
        DatabaseLoginInfo info = DatabaseLoginInfo.getLoginInfo();
        database = info.getDatabase();
        root = info.username();
        pass = info.password();
        SQLReader.runSQL("scripts\\DropTables.sql", GlobalQuery.getRootConnection());
    }

    private static final String recordsCSV = "records.csv";

    private static final String staffsCSV = "staffs.csv";

    private static IDatabaseManipulation manipulation = null;

    private static final String answerDirectory = "src/test/localAnswerSerFiles/";

    private static SUSTCDepartmentManagerUserTest sustcDepartmentManagerUserTest = null;

    private static CompanyManagerUserTest companyManagerUserTest = null;

    private static CourierUserTest courierUserTest = null;

    private static SeaportOfficerUserTest seaportOfficerUserTest = null;

    private static final LogInfo logInfo = new LogInfo("Hua Hang", LogInfo.StaffType.SustcManager, "500622842781782190");

    @BeforeAll
    public static void clearDatabaseAndPrepareAnswer() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + database;
        Properties props = new Properties();
        props.setProperty("user", root);
        props.setProperty("password", pass);
        try {
            Connection con = DriverManager.getConnection(url, props);
            con.setAutoCommit(true);
            Statement stmt1 = con.createStatement();
            Statement stmt2 = con.createStatement();
            ResultSet resultSet = stmt1.executeQuery("SELECT concat('DROP TABLE IF EXISTS ', table_name, ' CASCADE;') FROM information_schema.tables WHERE table_schema = 'public';");
            while (resultSet.next()) {
                stmt2.executeUpdate(resultSet.getString(1));
            }
            stmt1.close();
            stmt2.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }

        try {
            FileInputStream fis = new FileInputStream(answerDirectory + "SUSTCDepartmentManagerUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            sustcDepartmentManagerUserTest = (SUSTCDepartmentManagerUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream(answerDirectory + "CompanyManagerUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            companyManagerUserTest = (CompanyManagerUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream(answerDirectory + "CourierUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            courierUserTest = (CourierUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream(answerDirectory + "SeaportOfficerUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            seaportOfficerUserTest = (SeaportOfficerUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void createManipulation() {
        try {
            Class<?> clazz = Class.forName(manipulationImplClassName);
            manipulation = (IDatabaseManipulation) clazz.getDeclaredConstructor(String.class, String.class, String.class).newInstance(database, root, pass);
            assertNotNull(manipulation);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Order(2)
    @Timeout(value = 80000, unit = TimeUnit.MILLISECONDS)
    public void importData() {
        try {
            manipulation.$import(readFile(recordsCSV), readFile(staffsCSV));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getCompanyCount() {
        Set<Map.Entry<LogInfo, Integer>> entries = sustcDepartmentManagerUserTest.getCompanyCount.entrySet();
        for (Map.Entry<LogInfo, Integer> entry : entries) {
            assertEquals(entry.getValue(), manipulation.getCompanyCount(entry.getKey()));
        }
    }

    @Test
    @Order(4)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getCityCount() {
        Set<Map.Entry<LogInfo, Integer>> entries = sustcDepartmentManagerUserTest.getCityCount.entrySet();
        for (Map.Entry<LogInfo, Integer> entry : entries) {
            assertEquals(entry.getValue(), manipulation.getCityCount(entry.getKey()));
        }
    }

    @Test
    @Order(5)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getCourierCount() {
        Set<Map.Entry<LogInfo, Integer>> entries = sustcDepartmentManagerUserTest.getCourierCount.entrySet();
        for (Map.Entry<LogInfo, Integer> entry : entries) {
            assertEquals(entry.getValue(), manipulation.getCourierCount(entry.getKey()));
        }
    }

    @Test
    @Order(6)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getShipCount() {
        Set<Map.Entry<LogInfo, Integer>> entries = sustcDepartmentManagerUserTest.getShipCount.entrySet();
        for (Map.Entry<LogInfo, Integer> entry : entries) {
            assertEquals(entry.getValue(), manipulation.getShipCount(entry.getKey()));
        }
    }

    /**
     * Attention:
     * The item may not exist.
     */
    @Test
    @Order(7)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getItemInfo() {
        Set<Map.Entry<List<Object>, ItemInfo>> entries = sustcDepartmentManagerUserTest.getItemInfo.entrySet();
        for (Map.Entry<List<Object>, ItemInfo> entry : entries) {
            List<Object> params = entry.getKey();
            assertItemInfo(entry.getValue(), manipulation.getItemInfo((LogInfo) params.get(0), (String) params.get(1)));
        }
    }

    /**
     * Attention:
     * The ship may not exist.
     */
    @Test
    @Order(8)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getShipInfo() {
        Set<Map.Entry<List<Object>, ShipInfo>> entries = sustcDepartmentManagerUserTest.getShipInfo.entrySet();
        for (Map.Entry<List<Object>, ShipInfo> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.getShipInfo((LogInfo) params.get(0), (String) params.get(1)));
        }
    }

    /**
     * Attention:
     * The container may not exist.
     */
    @Test
    @Order(9)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getContainerInfo() {
        Set<Map.Entry<List<Object>, ContainerInfo>> entries = sustcDepartmentManagerUserTest.getContainerInfo.entrySet();
        for (Map.Entry<List<Object>, ContainerInfo> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.getContainerInfo((LogInfo) params.get(0), (String) params.get(1)));
        }
    }

    /**
     * Attention:
     * The staff may not exist.
     */
    @Test
    @Order(10)
    @Timeout(value = 600, unit = TimeUnit.MILLISECONDS)
    public void getStaffInfo() {
        Set<Map.Entry<List<Object>, StaffInfo>> entries = sustcDepartmentManagerUserTest.getStaffInfo.entrySet();
        for (Map.Entry<List<Object>, StaffInfo> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.getStaffInfo((LogInfo) params.get(0), (String) params.get(1)));
        }
    }

    /**
     * Attention:
     * 1. Duplicated item.
     * 2. Null value in some attributes (such as item name, class, etc.) is not allowed.
     * 3. The city relation between item and staff.
     */
    @Test
    @Order(11)
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    public void newItem() {
        Set<Map.Entry<List<Object>, Boolean>> entries = courierUserTest.newItem.entrySet();
        Set<Map.Entry<List<Object>, Boolean>> treeSet = new TreeSet<>((o1, o2) -> {
            Integer index1 = (Integer) o1.getKey().get(2);
            Integer index2 = (Integer) o2.getKey().get(2);
            return index1.compareTo(index2);
        });
        treeSet.addAll(entries);
        for (Map.Entry<List<Object>, Boolean> entry : treeSet) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.newItem((LogInfo) params.get(0), (ItemInfo) params.get(1)));
        }

        assertNotNull(manipulation.getItemInfo(logInfo, "newItem1"));
    }

    /**
     * Attention:
     * 1. The item may not exist.
     * 2. The old item state and the new item state.
     * 3. The city relation between item and staff.
     */
    @Test
    @Order(12)
    @Timeout(value = 1600, unit = TimeUnit.MILLISECONDS)
    public void setItemState() {
        Set<Map.Entry<List<Object>, Boolean>> entries = courierUserTest.setItemState.entrySet();
        for (Map.Entry<List<Object>, Boolean> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.setItemState((LogInfo) params.get(0), (String) params.get(1), (ItemState) params.get(2)));
        }

        ItemInfo itemInfo = manipulation.getItemInfo(logInfo, "newItem1");
        assertEquals(ItemState.ToExportTransporting, itemInfo.state());
    }

    @Test
    @Order(13)
    @Timeout(value = 400, unit = TimeUnit.MILLISECONDS)
    public void getImportTaxRate() {
        Set<Map.Entry<List<Object>, Double>> entries = companyManagerUserTest.getImportTaxRate.entrySet();
        for (Map.Entry<List<Object>, Double> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.getImportTaxRate((LogInfo) params.get(0), (String) params.get(1), (String) params.get(2)), 0.01);
        }
    }

    @Test
    @Order(14)
    @Timeout(value = 400, unit = TimeUnit.MILLISECONDS)
    public void getExportTaxRate() {
        Set<Map.Entry<List<Object>, Double>> entries = companyManagerUserTest.getExportTaxRate.entrySet();
        for (Map.Entry<List<Object>, Double> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.getExportTaxRate((LogInfo) params.get(0), (String) params.get(1), (String) params.get(2)), 0.01);
        }
    }

    /**
     * Attention:
     * 1. The item state.
     * 2. The container is whether using.
     * 3. The company relation between item's retrieval courier and log staff.
     */
    @Test
    @Order(15)
    @Timeout(value = 1500, unit = TimeUnit.MILLISECONDS)
    public void loadItemToContainer() {
        Set<Map.Entry<List<Object>, Boolean>> entries = companyManagerUserTest.loadItemToContainer.entrySet();
        for (Map.Entry<List<Object>, Boolean> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.loadItemToContainer((LogInfo) params.get(0), (String) params.get(1), (String) params.get(2)));
        }

        ContainerInfo containerInfo = manipulation.getContainerInfo(logInfo, "c8458f8d");
        assertTrue(containerInfo.using());
    }

    /**
     * Attention:
     * 1. The item state.
     * 2. The ship is whether sailing.
     * 3. The company relation between ship and log staff.
     */
    @Test
    @Order(16)
    @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
    public void loadContainerToShip() {
        Set<Map.Entry<List<Object>, Boolean>> entries = companyManagerUserTest.loadContainerToShip.entrySet();
        for (Map.Entry<List<Object>, Boolean> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.loadContainerToShip((LogInfo) params.get(0), (String) params.get(1), (String) params.get(2)));
        }

        ItemInfo itemInfo = manipulation.getItemInfo(logInfo, "banana-136c");
        assertEquals(ItemState.WaitingForShipping, itemInfo.state());
    }

    /**
     * Attention:
     * 1. The item state.
     * 2. The ship is whether sailing.
     * 3. The company relation between ship and log staff.
     */
    @Test
    @Order(17)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void shipStartSailing() {
        Set<Map.Entry<List<Object>, Boolean>> entries = companyManagerUserTest.shipStartSailing.entrySet();
        for (Map.Entry<List<Object>, Boolean> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.shipStartSailing((LogInfo) params.get(0), (String) params.get(1)));
        }

        ItemInfo itemInfo = manipulation.getItemInfo(logInfo, "banana-136c");
        assertEquals(ItemState.Shipping, itemInfo.state());
    }

    /**
     * Attention:
     * 1. The item state.
     * 2. The ship is whether sailing.
     * 3. The company relation between item's retrieval courier and log staff.
     */
    @Test
    @Order(18)
    @Timeout(value = 1200, unit = TimeUnit.MILLISECONDS)
    public void unloadItem() {
        Set<Map.Entry<List<Object>, Boolean>> entries = companyManagerUserTest.unloadItem.entrySet();
        for (Map.Entry<List<Object>, Boolean> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.unloadItem((LogInfo) params.get(0), (String) params.get(1)));
        }

        ItemInfo itemInfo = manipulation.getItemInfo(logInfo, "coconut-39c9e");
        assertEquals(ItemState.UnpackingFromContainer, itemInfo.state());
    }

    /**
     * Attention:
     * 1. The item state.
     * 2. The company relation between item's retrieval courier and log staff.
     */
    @Test
    @Order(19)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void itemWaitForChecking() {
        Set<Map.Entry<List<Object>, Boolean>> entries = companyManagerUserTest.itemWaitForChecking.entrySet();
        for (Map.Entry<List<Object>, Boolean> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.itemWaitForChecking((LogInfo) params.get(0), (String) params.get(1)));
        }

        ItemInfo itemInfo = manipulation.getItemInfo(logInfo, "pear-c52a3");
        assertEquals(ItemState.ImportChecking, itemInfo.state());
    }

    @Test
    @Order(20)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void getAllItemsAtPort() {
        Set<Map.Entry<LogInfo, String[]>> entries = seaportOfficerUserTest.getAllItemsAtPort.entrySet();
        for (Map.Entry<LogInfo, String[]> entry : entries) {
            Set<String> expected = new HashSet<>(Arrays.asList(entry.getValue()));
            Set<String> actual = new HashSet<>(Arrays.asList(manipulation.getAllItemsAtPort(entry.getKey())));
            assertEquals(expected, actual);
        }
    }

    /**
     * Attention:
     * 1. The item state.
     * 2. The relation between import/export city and log staff city.
     */
    @Test
    @Order(21)
    @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
    public void setItemCheckState() {
        Set<Map.Entry<List<Object>, Boolean>> entries = seaportOfficerUserTest.setItemCheckState.entrySet();
        for (Map.Entry<List<Object>, Boolean> entry : entries) {
            List<Object> params = entry.getKey();
            assertEquals(entry.getValue(), manipulation.setItemCheckState((LogInfo) params.get(0), (String) params.get(1), (Boolean) params.get(2)));
        }

        ItemInfo itemInfo = manipulation.getItemInfo(logInfo, "banana-1351");
        assertEquals(ItemState.PackingToContainer, itemInfo.state());
        assertEquals("Tong Gu", itemInfo.export().officer());
    }

    public String readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        reader.lines().forEach(l -> {
            sb.append(l);
            sb.append("\n");
        });
        return sb.toString();
    }

    public void assertItemInfo(ItemInfo a, ItemInfo b) {
        if (a == null && b == null) return;
        if ((a != null && b == null) || (a == null && b != null)) fail();
        double EPS = 0.0001;
        assertEquals(a.name(), b.name());
        assertEquals(a.$class(), b.$class());
        assertTrue(Math.abs(a.price() - b.price()) / a.price() < EPS);
        assertEquals(a.state(), b.state());
        assertEquals(a.retrieval().city(), b.retrieval().city());
        assertEquals(a.retrieval().courier(), b.retrieval().courier());
        assertEquals(a.delivery().city(), b.delivery().city());
        assertEquals(a.delivery().courier(), b.delivery().courier());
        assertEquals(a.$import().city(), b.$import().city());
        assertEquals(a.$import().officer(), b.$import().officer());
        assertTrue(Math.abs(a.$import().tax() - b.$import().tax()) / a.$import().tax() < EPS);
        assertEquals(a.export().city(), b.export().city());
        assertEquals(a.export().officer(), b.export().officer());
        assertTrue(Math.abs(a.export().tax() - b.export().tax()) / a.export().tax() < EPS);
    }
}