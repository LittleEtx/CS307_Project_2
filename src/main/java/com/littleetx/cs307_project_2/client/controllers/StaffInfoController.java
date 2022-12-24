package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.database.DatabaseMapping;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.interfaces.StaffInfo;
import org.controlsfx.control.PopOver;

public class StaffInfoController {
    public static final String CHANGE_PASSWORD_FXML = "ChangePassword.fxml";
    public static final String PERSONAL_INFO_FXML = "PersonalInfo.fxml";
    @FXML
    protected Text authorization;

    @FXML
    protected Text name;
    @FXML
    protected Text ID;
    @FXML
    protected HBox infoHbox;
    @FXML
    protected HBox cityBox;
    @FXML
    protected HBox companyBox;

    @FXML
    protected Text city;
    @FXML
    protected Text company;
    @FXML
    protected Node infoButton;

    @FXML
    protected Node logoutButton;
    @FXML
    protected Node changePasswordButton;


    @FXML
    private void initialize() {
        StaffInfo staffInfo = GlobalManager_Client.getStaffInfo();
        authorization.setText(DatabaseMapping.getStaffAuthorityVisualStr(staffInfo.basicInfo().type()));
        name.setText(staffInfo.basicInfo().name());
        ID.setText(Integer.toString(GlobalManager_Client.getStaffID()));
        if (staffInfo.city() != null) {
            city.setText(staffInfo.city());
        } else {
            infoHbox.getChildren().remove(cityBox);
        }
        if (staffInfo.company() != null) {
            company.setText(staffInfo.company());
        } else {
            infoHbox.getChildren().remove(companyBox);
        }

        logoutButton.setOnMouseEntered(event -> logoutButton.setOpacity(0.2));
        logoutButton.setOnMouseExited(event -> logoutButton.setOpacity(1));
        changePasswordButton.setOnMouseEntered(event -> changePasswordButton.setOpacity(0.2));
        changePasswordButton.setOnMouseExited(event -> changePasswordButton.setOpacity(1));
        infoButton.setOnMouseEntered(event -> infoButton.setOpacity(0.2));
        infoButton.setOnMouseExited(event -> infoButton.setOpacity(1));
    }

    @FXML
    private void onLogout() {
        GlobalManager_Client.showConfirm("Are you sure to logout?", result -> {
            if (result) {
                GlobalManager_Client.enterLoginInterface();
            }
        });
    }

    @FXML
    private void onChangePassword() {
        GlobalManager_Client.showWindow("ChangePassword",
                CHANGE_PASSWORD_FXML, 400, 300);
    }

    @FXML
    private void onInfo() {
        PopOver popOver = new PopOver();
        popOver.setTitle("Personal Information");
        popOver.setCloseButtonEnabled(false);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setContentNode(GlobalManager_Client.readXML(PERSONAL_INFO_FXML));
        popOver.show(infoButton);
    }
}
