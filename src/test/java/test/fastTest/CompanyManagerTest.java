package test.fastTest;

import test.answers.CompanyManagerUserTest;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class CompanyManagerTest {
    public static void main(String[] args) {
        CompanyManagerUserTest test;
        try {
            FileInputStream fis = new FileInputStream("src/test/localAnswerSerFiles/CompanyManagerUserTest.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            test = (CompanyManagerUserTest) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
