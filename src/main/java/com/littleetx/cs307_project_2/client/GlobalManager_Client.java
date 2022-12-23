package com.littleetx.cs307_project_2.client;

import com.littleetx.cs307_project_2.client.dialogs.AlertDialog;
import com.littleetx.cs307_project_2.client.dialogs.ConfirmDialog;
import com.littleetx.cs307_project_2.database.DatabaseMapping;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.interfaces.StaffInfo;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;

public class GlobalManager_Client {
    private static final String LOGIN_FXML = "Login.fxml";
    private static final String STAFF_INFO_FXML = "StaffInfo.fxml";
    private static final String COURIER_FXML = "Courier.fxml";
    private static final String SUSTC_MANAGER_FXML = "SUSTCManager.fxml";
    private static final String SEAPORT_OFFICER_FXML = "SeaportOfficer.fxml";
    private static final String COMPANY_MANAGER_FXML = "CompanyManager.fxml";
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
        switch (staffInfo.basicInfo().type()) {
            case Courier -> changeScene(readXML(COURIER_FXML));
            case SustcManager -> changeScene(readXML(SUSTC_MANAGER_FXML));
            case SeaportOfficer -> changeScene(readXML(SEAPORT_OFFICER_FXML));
            case CompanyManager -> changeScene(readXML(COMPANY_MANAGER_FXML));
            default -> throw new RuntimeException("Unknown staff type");
        }
    }

    private static void changeScene(Parent node) {
        Scene scene = new Scene(node, 800, 600);
        stage.close();
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("SUSTC DMS, " +
                DatabaseMapping.getStaffAuthorityVisualStr(staffInfo.basicInfo().type()) + " : " +
                staffID + ", " + staffInfo.basicInfo().name());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    private static final Stack<Stage> stageStack = new Stack<>();

    public static void showWindow(String title, String contentPath, int minWidth, int minHeight) {
        Stage dialog = new Stage();
        dialog.setTitle(title);
        dialog.setScene(new Scene(readXML(contentPath), minWidth, minHeight));
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setMinWidth(minWidth);
        dialog.setMinHeight(minHeight);
        dialog.show();
        stageStack.push(dialog);
    }

    public static void closeWindow() {
        if (stageStack.isEmpty()) {
            System.out.println("Warning: No window to close!");
            return;
        }

        Stage dialog = stageStack.pop();
        if (dialog != null) {
            dialog.close();
        }
    }

    public static void closeMainWindow() {
        stage.close();
    }

    public static void enterLoginInterface() {
        stage.close();
        stage = new Stage();
        Parent root = readXML(LOGIN_FXML);
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("SUSTC Database Management System");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    private static boolean inLostConnectionShowing = false;

    public static void lostConnection() {
        if (inLostConnectionShowing) {
            return;
        }
        System.out.println("Lost connection!");
        stage.setOpacity(0.8);
        showAlert("Lost connection!", () -> {
            inLostConnectionShowing = false;
            enterLoginInterface();
        });
        inLostConnectionShowing = true;
    }

    public static void showAlert(String msg, Runnable callback) {
        showDialog(new AlertDialog(msg, callback), "Warning", callback);
    }

    public static void showAlert(String msg) {
        showDialog(new AlertDialog(msg, null), "Warning", null);
    }

    public static void showConfirm(String msg, Consumer<Boolean> callback) {
        showDialog(new ConfirmDialog(msg, callback), "Confirm",
                () -> callback.accept(false));
    }

    private static void showDialog(Parent node, String title, Runnable onClose) {
        Stage dialog = new Stage();
        dialog.setTitle(title);
        dialog.setScene(new Scene(node));
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.setOnCloseRequest(event -> {
            if (onClose != null) {
                onClose.run();
            }
        });
        dialog.show();
        stageStack.push(dialog);
    }
}
