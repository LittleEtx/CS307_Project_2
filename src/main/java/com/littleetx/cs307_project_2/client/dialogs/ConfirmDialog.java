package com.littleetx.cs307_project_2.client.dialogs;

import javafx.scene.image.ImageView;

import java.util.function.Consumer;

public class ConfirmDialog extends DialogBase {
    public ConfirmDialog(String message, Consumer<Boolean> callback) {
        super(new ImageView("com/littleetx/cs307_project_2/assets/icons/question.png"), message);
        addButton("Yes", () -> callback.accept(true),
                "com/littleetx/cs307_project_2/assets/css/ButtonCSS.css", "ok-button");
        addButton("Cancel", () -> callback.accept(false));
    }
}
