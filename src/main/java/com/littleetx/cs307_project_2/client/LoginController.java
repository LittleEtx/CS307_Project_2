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
    public void initialize() {
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
            IServerProtocol connection = ClientHelper.getConnection();

            showWaiting();
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
            pause.setOnFinished(event -> {
                try {
                    if (!connection.verify(loginUserTextField.getText(),
                            loginPasswordField.getText())) {
                        loginUserTextField.setDisable(false);
                        loginPasswordField.setDisable(false);
                        loginPasswordField.clear();
                        showMessage("Invalid username/ID or password!");
                    } else {
                        System.out.printf("Login success!%n");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException("Lost connection to the server!", e);
                }
            });

        } catch (MalformedURLException | NotBoundException e) {
            showMessage("Unable to connect to the server!");
        } catch (RemoteException e) {
            throw new RuntimeException("Lost connection to the server!", e);
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
