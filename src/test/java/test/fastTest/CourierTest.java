package test.fastTest;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.database.user.SUSTCManager;
import main.interfaces.ItemInfo;
import main.interfaces.LogInfo;
import test.answers.CourierUserTest;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CourierTest {
    public static void main(String[] args) {
        CourierUserTest test;
        try {
            FileInputStream fis = new FileInputStream("src/test/localAnswerSerFiles/CourierUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            test = (CourierUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        var courier = verification.getUser(new LogInfo(
                        "Man Qimao", LogInfo.StaffType.Courier, "3778176922463414481"),
                Courier.class);

        Set<Map.Entry<List<Object>, Boolean>> entries = test.newItem.entrySet();
        Set<Map.Entry<List<Object>, Boolean>> treeSet = new TreeSet<>((o1, o2) -> {
            Integer index1 = (Integer) o1.getKey().get(2);
            Integer index2 = (Integer) o2.getKey().get(2);
            return index1.compareTo(index2);
        });
        treeSet.addAll(entries);
        for (Map.Entry<List<Object>, Boolean> entry : treeSet) {
            List<Object> params = entry.getKey();
            if (entry.getValue() != courier.newItem(((ItemInfo) params.get(1))))
                System.out.println("newItem failed");
        }
        SUSTCManager sustcManager = verification.getUser(10003386, SUSTCManager.class);
        System.out.println(sustcManager.getItemInfo("newItem1"));
    }
}
