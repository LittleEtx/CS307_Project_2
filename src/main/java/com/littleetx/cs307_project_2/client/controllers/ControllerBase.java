package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ControllerBase {
    @FXML
    protected TabPane tabPane;

    @FXML
    protected VBox rootVBox;

    @FXML
    protected void initialize() {
        rootVBox.getChildren().add(
                0, GlobalManager_Client.getStaffInfoPanel());
    }

    protected void initialTable(TableView<?> table) {
        HBox.setHgrow(table, javafx.scene.layout.Priority.ALWAYS);
    }

    protected void refreshTable(Runnable refresh) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.01));
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        pause.setOnFinished(event -> {
            if (tabPane.getSelectionModel().getSelectedItem() != currentTab)
                return;

            //set disable
            tabPane.setDisable(true);
            PauseTransition refreshPause = new PauseTransition(Duration.seconds(0.01));
            refreshPause.setOnFinished(event1 -> {
                refresh.run();
                tabPane.setDisable(false);
            });
            refreshPause.play();
        });
        pause.play();
    }
}
