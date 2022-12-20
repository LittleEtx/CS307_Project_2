package test.fastTest;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.SUSTCManager;
import main.interfaces.ItemInfo;
import test.answers.SUSTCDepartmentManagerUserTest;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class SUSTCManagerTest {
    public static void main(String[] args) {
        SUSTCDepartmentManagerUserTest test = new SUSTCDepartmentManagerUserTest();
        try {
            FileInputStream fis = new FileInputStream("src/test/localAnswerSerFiles/SUSTCDepartmentManagerUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            test = (SUSTCDepartmentManagerUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        SUSTCManager sustcManager = verification.getUser(10003386, SUSTCManager.class);

        System.out.println(sustcManager.getItemInfo("newItem1"));

        for (var entry : test.getItemInfo.entrySet()) {
            List<Object> params = entry.getKey();
            ItemInfo result = sustcManager.getItemInfo((String) params.get(1));
            if (!entry.getValue().equals(result)) {
                System.out.println("getItemInfo failed");
            }
        }


    }
}
