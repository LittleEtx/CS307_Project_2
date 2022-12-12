package com.littleetx.cs307_project_2.client;

import cs307.project2.interfaces.StaffInfo;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

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
    private void initialize() {
        StaffInfo staffInfo = ClientGlobalManager.getStaffInfo();
        authorization.setText(staffInfo.basicInfo().type().toString());
        name.setText(staffInfo.basicInfo().name());
        ID.setText(Integer.toString(ClientGlobalManager.getStaffID()));
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
    }


}
