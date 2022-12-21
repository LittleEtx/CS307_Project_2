package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.database.DatabaseMapping;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.interfaces.StaffInfo;

public class StaffInfoController {
    @FXML
    protected Text authorization;

    @FXML
    protected Text name;
    @FXML
    protected Text ID;
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
            cityBox.setVisible(true);
        } else {
            cityBox.setVisible(false);
        }
        if (staffInfo.company() != null) {
            company.setText(staffInfo.company());
            companyBox.setVisible(true);
        } else {
            companyBox.setVisible(false);
        }

        logoutButton.setOnMouseEntered(event -> logoutButton.setOpacity(0.2));
        logoutButton.setOnMouseExited(event -> logoutButton.setOpacity(1));
        changePasswordButton.setOnMouseEntered(event -> changePasswordButton.setOpacity(0.2));
        changePasswordButton.setOnMouseExited(event -> changePasswordButton.setOpacity(1));
        infoButton.setOnMouseEntered(event -> infoButton.setOpacity(0.2));
        infoButton.setOnMouseExited(event -> infoButton.setOpacity(1));
    }
}
