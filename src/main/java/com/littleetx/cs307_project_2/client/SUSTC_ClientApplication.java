package com.littleetx.cs307_project_2.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class SUSTC_ClientApplication extends Application {

    @Override
    public void start(Stage stage) {
        ClientGlobalManager.setStage(stage);
        ClientGlobalManager.enterLoginInterface();
    }

    public static void main(String[] args) {
        launch();
    }

}