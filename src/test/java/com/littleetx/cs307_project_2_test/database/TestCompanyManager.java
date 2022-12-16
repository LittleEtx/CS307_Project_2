package com.littleetx.cs307_project_2_test.database;

import com.littleetx.cs307_project_2.database.DatabaseLoginInfo;
import com.littleetx.cs307_project_2.database.Verification;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
import main.DatabaseManipulation;
import main.interfaces.LogInfo;
import test.answers.CompanyManagerUserTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class TestCompanyManager {
    public static void main(String[] args) {
        testBasingOnData();
        //basicTest();
    }

    private static void testBasingOnData() {
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        CompanyManager manager = verification.getUser(
                verification.checkAuthority("10203261", "9084612030962347462"), CompanyManager.class);

        CompanyManagerUserTest test;
        try {
            FileInputStream fis = new FileInputStream("src/test/localAnswerSerFiles/CompanyManagerUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            test = (CompanyManagerUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        for (var entry : test.getImportTaxRate.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var result = verification.getUser((LogInfo) key.get(0), CompanyManager.class)
                    .getTaxRate((String) key.get(1), (String) key.get(2), CompanyManager.TaxType.Import);
            if (Math.abs(result - value) > 0.01) {
                System.out.println("Wrong import tax rate for " + key.get(0) + " city: " + key.get(1) + " item: " + key.get(2));
                System.out.println("Expected: " + value);
                System.out.println("Actual: " + result);
            }
        }
    }

    private static void basicTest() {
        Verification verification = new Verification(DatabaseLoginInfo.getLoginInfo());
        CompanyManager manager = verification.getUser(
                verification.checkAuthority("10203261", "9084612030962347462"), CompanyManager.class);
        System.out.println(manager.getTaxRate(12, "peach", CompanyManager.TaxType.Import));
        System.out.println(manager.getTaxRate(12, "peach", CompanyManager.TaxType.Export));

        LogInfo logInfo = new LogInfo("10203261", LogInfo.StaffType.CompanyManager, "9084612030962347462");

        DatabaseLoginInfo info = DatabaseLoginInfo.getLoginInfo();
        DatabaseManipulation databaseManipulation = new DatabaseManipulation(
                info.getDatabase(), info.username(), info.password(), false
        );
        databaseManipulation.getImportTaxRate(logInfo, "Shanghai", "cherry");
    }


}
