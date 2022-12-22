package com.littleetx.cs307_project_2_test.client;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import javafx.application.Application;
import javafx.stage.Stage;

public class DialogTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        GlobalManager_Client.setStage(primaryStage);
        GlobalManager_Client.showAlert("Hello World!", () -> {
            GlobalManager_Client.showConfirm("Are you sure?", yes -> {
                System.out.println(yes ? "Yes" : "No");
            });
        });

    }

    public static void main(String[] args) {
        launch();
    }
}
