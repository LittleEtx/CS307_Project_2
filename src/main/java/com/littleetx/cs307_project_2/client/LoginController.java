package com.littleetx.cs307_project_2.client;

import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Text loginMessage;
    @FXML
    private TextField loginUserTextField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private VBox loginVBox;
    @FXML
    private ProgressIndicator loginProgress;

    @FXML
    protected void initialize() {
        loginVBox.getChildren().clear();
        loginButton.setDisable(true);
        loginVBox.getChildren().addAll(loginButton);

        loginUserTextField.textProperty().addListener((observable, oldValue, newValue)
                -> loginButton.setDisable(newValue.isEmpty() || loginPasswordField.getText().isEmpty()));
        loginPasswordField.textProperty().addListener((observable, oldValue, newValue)
                -> loginButton.setDisable(newValue.isEmpty() || loginUserTextField.getText().isEmpty()));
    }

    @FXML
    protected void onLoginButtonClick() {
        try {
            IServerProtocol server = ClientHelper.getConnection();

            showWaiting();
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
            pause.setOnFinished(event -> {
                System.out.println("Login: " + loginUserTextField.getText() + " " + loginPasswordField.getText());
                try {
                    if (!server.verify(loginUserTextField.getText(),
                            loginPasswordField.getText())) {
                        loginUserTextField.setDisable(false);
                        loginPasswordField.setDisable(false);
                        loginPasswordField.clear();
                        showMessage("Invalid username/ID or password!");
                    } else {
                        GlobalManager_Client.enterUserInterface(server.getStaffID(), server.getStaffInfo());
                    }
                } catch (RemoteException e) {
                    System.out.println("Lost connection to the server!");
                    e.printStackTrace();
                }
            });
            pause.play();

        } catch (MalformedURLException | NotBoundException e) {
            showMessage("Unable to connect to the server!");
        } catch (RemoteException e) {
            System.out.println("Lost connection to the server!");
            e.printStackTrace();
        }
    }

    private void showWaiting() {
        loginVBox.getChildren().remove(loginMessage);
        loginVBox.getChildren().remove(loginButton);
        loginUserTextField.setDisable(true);
        loginPasswordField.setDisable(true);
        loginVBox.getChildren().add(loginProgress);
    }

    private void showMessage(String msg) {
        loginVBox.getChildren().clear();
        loginMessage.setText(msg);
        loginVBox.getChildren().add(loginMessage);
        loginVBox.getChildren().add(loginButton);
    }
}
