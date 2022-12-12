package com.littleetx.cs307_project_2.client;

import cs307.project2.interfaces.LogInfo;
import cs307.project2.interfaces.StaffInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GlobalManager_Client {
    private static final String LOGIN_FXML = "Login.fxml";
    private static final String STAFF_INFO_FXML = "StaffInfo.fxml";
    private static final String COURIER_FXML = "Courier.fxml";
    private static StaffInfo staffInfo;
    private static int staffID;
    private static Stage stage;

    public static void setStage(Stage stage) {
        GlobalManager_Client.stage = stage;
    }

    public static StaffInfo getStaffInfo() {
        return staffInfo;
    }

    public static int getStaffID() {
        return staffID;
    }

    private static Parent readXML(String fxml) {
        try {
            return FXMLLoader.load(Objects.requireNonNull(
                    GlobalManager_Client.class.getResource(fxml)));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Can not load " + fxml, e);
        }
    }

    public static Node getStaffInfoPanel() {
        return readXML(STAFF_INFO_FXML);
    }

    public static void enterUserInterface(int staffID, StaffInfo staffInfo) {
        System.out.println("enterUserInterface");
        GlobalManager_Client.staffInfo = staffInfo;
        GlobalManager_Client.staffID = staffID;
        if (staffInfo.basicInfo().type() == LogInfo.StaffType.Courier) {
            changeScene(readXML(COURIER_FXML));
        }
    }

    private static void changeScene(Parent node) {
        Scene scene = new Scene(node);
        stage.close();
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void enterLoginInterface() {
        Parent root = readXML(LOGIN_FXML);
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("SUSTC Database Management System");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }
}
