package com.littleetx.cs307_project_2_test.client;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.database.GlobalQuery;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Objects;

public class CompanyManagerTest extends Application {
    private static final int id = 10503279;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GlobalManager_Client.setStage(primaryStage);
        GlobalManager_Client.enterUserInterface(id,
                Objects.requireNonNull(GlobalQuery.getStaffInfo(id)));
    }

    public static void main(String[] args) {
        launch();
    }
}
