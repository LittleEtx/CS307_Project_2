package com.littleetx.cs307_project_2.client;

import com.littleetx.cs307_project_2.client.controllers.GlobalManager_Client;
import javafx.application.Application;
import javafx.stage.Stage;

public class SUSTC_ClientApplication extends Application {

    @Override
    public void start(Stage stage) {
        GlobalManager_Client.setStage(stage);
        GlobalManager_Client.enterLoginInterface();
    }

    public static void main(String[] args) {
        launch();
    }

}