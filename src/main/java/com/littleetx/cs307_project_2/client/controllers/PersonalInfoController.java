package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.interfaces.StaffInfo;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PersonalInfoController {
    @FXML
    private Text gender;
    @FXML
    private Text age;
    @FXML
    private Text phone;

    @FXML
    private Node changePhoneButton;

    @FXML
    private HBox phoneBox;

    @FXML
    protected void initialize() {
        phone.sceneProperty().addListener(observable -> {
            if (phone.getScene() != null) {
                phone.getScene().getStylesheets().add(
                        "com/littleetx/cs307_project_2/assets/css/validation.css");
            }
        });


        StaffInfo info = GlobalManager_Client.getStaffInfo();
        gender.setText(info.isFemale() ? "Female" : "Male");
        age.setText(Integer.toString(info.age()));
        phone.setText(info.phoneNumber());

        changePhoneButton.setOnMouseEntered(event -> changePhoneButton.setOpacity(0.2));
        changePhoneButton.setOnMouseExited(event -> changePhoneButton.setOpacity(1));
    }

    @FXML
    private void changePhone() {
        TextField textField = new TextField();
        phoneBox.getChildren().removeAll(changePhoneButton, phone);
        phoneBox.getChildren().add(textField);

        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.setValidationDecorator(new StyleClassValidationDecoration());
        validationSupport.registerValidator(textField, false,
                Validator.createRegexValidator("Phone number should be 11 digits",
                        "\\d{11}", Severity.ERROR));

        Runnable actionEventEventHandler = () -> {
            if (!phoneBox.getChildren().contains(textField)) {
                return;
            }

            if (!validationSupport.isInvalid()) {
                GlobalManager_Client.setStaffPhoneNumber(textField.getText());
                try {
                    IServerProtocol server = ClientHelper.getConnection();
                    server.changePhoneNumber(GlobalManager_Client.getStaffID(), textField.getText());
                } catch (MalformedURLException | NotBoundException | RemoteException e) {
                    GlobalManager_Client.lostConnection();
                }
            }
            phone.setText(GlobalManager_Client.getStaffInfo().phoneNumber());
            phoneBox.getChildren().remove(textField);
            phoneBox.getChildren().addAll(phone, changePhoneButton);
        };
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                actionEventEventHandler.run();
            }
        });
        textField.setOnAction(event -> actionEventEventHandler.run());
    }
}
