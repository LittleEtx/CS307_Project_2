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

public class ClientGlobalManager {
    private static final String LOGIN_FXML = "Login.fxml";
    private static final String STAFF_INFO_FXML = "StaffInfo.fxml";
    private static final String COURIER_FXML = "Courier.fxml";
    private static StaffInfo staffInfo;
    private static int staffID;
    private static Stage stage;

    public static void setStage(Stage stage) {
        ClientGlobalManager.stage = stage;
    }

    public static StaffInfo getStaffInfo() {
        return staffInfo;
    }

    public static int getStaffID() {
        return staffID;
    }

    private static class StaffInfoInterface {
        private static final Node node;

        static {
            try {
                node = FXMLLoader.load(Objects.requireNonNull(
                        ClientGlobalManager.class.getResource(STAFF_INFO_FXML)));
            } catch (IOException | NullPointerException e) {
                throw new RuntimeException("Can not load " + STAFF_INFO_FXML, e);
            }
        }
    }

    public static void enterUserInterface(StaffInfo staffInfo, int staffID) {
        ClientGlobalManager.staffInfo = staffInfo;
        ClientGlobalManager.staffID = staffID;
        if (staffInfo.basicInfo().type() == LogInfo.StaffType.Courier) {
            changeScene(CourierInterface.scene);
        }
    }

    private static void changeScene(Scene scene) {
        stage.close();
        stage.setScene(scene);
        stage.setMinWidth(scene.getWidth());
        stage.setMinHeight(scene.getHeight());
        stage.show();
    }

    private static class CourierInterface {
        private static final Scene scene;

        static {
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(
                        ClientGlobalManager.class.getResource(COURIER_FXML)));
                scene = new Scene(root, 800, 600);
            } catch (IOException | NullPointerException e) {
                throw new RuntimeException("cannot read: " + COURIER_FXML, e);
            }
        }
    }

    private static class LoginInterface {
        private static final Scene scene;

        static {
            try {
                Parent root = FXMLLoader.load(
                        ClientGlobalManager.class.getResource(LOGIN_FXML));
                scene = new Scene(root, 600, 400);
            } catch (IOException e) {
                throw new RuntimeException("cannot read: " + LOGIN_FXML, e);
            }
        }
    }

    public static void enterLoginInterface() {
        stage.setScene(LoginInterface.scene);
        stage.setTitle("SUSTC Database Management System");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }
}
