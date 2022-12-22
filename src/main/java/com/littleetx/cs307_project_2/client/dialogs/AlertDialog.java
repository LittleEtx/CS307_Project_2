package com.littleetx.cs307_project_2.client.dialogs;

import javafx.scene.image.ImageView;

public class AlertDialog extends DialogBase {
    public AlertDialog(String msg, Runnable callback) {
        super(new ImageView("com/littleetx/cs307_project_2/assets/icons/exclamation.png"), msg);
        addButton("OK", callback,
                "com/littleetx/cs307_project_2/assets/css/ButtonCSS.css", "ok-button");
    }
}
