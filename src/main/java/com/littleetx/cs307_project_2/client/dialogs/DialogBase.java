package com.littleetx.cs307_project_2.client.dialogs;

import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DialogBase extends VBox {
    protected HBox buttonBox;

    public DialogBase(ImageView img, String msg) {
        super(10);
        setPrefSize(400, 200);
        setPadding(new Insets(20, 40, 20, 20));

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        getChildren().add(hBox);
        VBox.setVgrow(hBox, Priority.ALWAYS);
        img.setFitWidth(64);
        img.setFitHeight(64);
        Text text = new Text(msg);
        hBox.getChildren().addAll(img, text);

        Separator separator = new Separator();
        getChildren().add(separator);

        buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().add(buttonBox);
    }

    protected void addButton(String text, Runnable onAction, String styleSheet, String styleClass) {
        Button okButton = new Button(text);
        okButton.setMinSize(60, 30);
        buttonBox.getChildren().add(okButton);
        if (styleSheet != null) {
            okButton.getStylesheets().add(styleSheet);
        }
        if (styleClass != null) {
            okButton.getStyleClass().add(styleClass);
        }
        okButton.setOnAction(event -> {
            GlobalManager_Client.closeWindow();
            if (onAction != null) {
                onAction.run();
            }
        });
    }

    protected void addButton(String text, Runnable onAction) {
        addButton(text, onAction, null, null);
    }
}
