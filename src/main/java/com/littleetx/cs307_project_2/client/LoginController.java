package com.littleetx.cs307_project_2.client;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Text loginFailedText;
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
        loginVBox.getChildren().addAll(loginButton);
    }
    @FXML
    protected void onLoginButtonClick() {
        loginVBox.getChildren().remove(loginFailedText);
        loginVBox.getChildren().remove(loginButton);
        loginUserTextField.setDisable(true);
        loginPasswordField.setDisable(true);
        loginVBox.getChildren().add(loginProgress);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            loginVBox.getChildren().clear();
            loginVBox.getChildren().add(loginFailedText);
            loginUserTextField.setDisable(false);
            loginPasswordField.setDisable(false);
            loginPasswordField.clear();
            loginVBox.getChildren().add(loginButton);
        });
        pause.play();
    }
}
