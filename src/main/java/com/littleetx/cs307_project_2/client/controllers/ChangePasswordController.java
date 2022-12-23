package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class ChangePasswordController {
    @FXML
    private TextField oldPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Node confirmBtn;

    @FXML
    private Text errorText;

    @FXML
    private void initialize() {
        //add style class to the root node
        confirmBtn.sceneProperty().addListener(observable -> {
            if (confirmBtn.getScene() != null) {
                confirmBtn.getScene().getStylesheets().add(
                        "com/littleetx/cs307_project_2/assets/css/validation.css");
            }
        });

        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.setValidationDecorator(new StyleClassValidationDecoration());
        validationSupport.registerValidator(oldPassword, false,
                Validator.createEqualsValidator(
                        "Old password is not correct", List.of(GlobalManager_Client
                                .getStaffInfo().basicInfo().password())));

        validationSupport.registerValidator(newPassword, false,
                (Validator<String>) (control, value) -> ValidationResult
                        .fromErrorIf(control, "Password must be at least 6 characters long",
                                value.length() < 6)
                        .addErrorIf(control, "Password cannot be identical to the old one",
                                value.equals(oldPassword.getText())));


        validationSupport.registerValidator(confirmPassword, false,
                Validator.createPredicateValidator(pass ->
                                !newPassword.getText().isEmpty() &&
                                        Objects.equals(pass, newPassword.getText()),
                        "Passwords do not match"));

        validationSupport.validationResultProperty().addListener((observable, oldValue, newValue) ->
                validationSupport.getHighestMessage(oldPassword)
                        .or(() -> validationSupport.getHighestMessage(newPassword))
                        .or(() -> validationSupport.getHighestMessage(confirmPassword))
                        .ifPresentOrElse(msg -> {
                            errorText.setText(msg.getText());
                            errorText.setVisible(true);
                        }, () -> errorText.setVisible(false)));
        confirmBtn.disableProperty().bind(validationSupport.invalidProperty());

        newPassword.textProperty().addListener((observable, oldValue, newValue) ->
                validationSupport.revalidate());
    }

    @FXML
    private void onConfirm() {
        try {
            if (ClientHelper.getConnection().changePassword(
                    GlobalManager_Client.getStaffID(), newPassword.getText())) {
                GlobalManager_Client.closeWindow();
            } else {
                GlobalManager_Client.showAlert("Failed to change password!");
            }
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            GlobalManager_Client.lostConnection();
        }
    }


}
