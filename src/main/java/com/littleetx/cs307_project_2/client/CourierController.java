package com.littleetx.cs307_project_2.client;

import com.littleetx.cs307_project_2.client.tables.ItemTableView;
import cs307.project2.interfaces.ItemInfo;
import cs307.project2.interfaces.ItemState;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.littleetx.cs307_project_2.client.tables.ItemTableView.*;

public class CourierController {
    @FXML
    private VBox rootVBox;
    @FXML
    private HBox newItemHBox;
    @FXML
    private HBox onGoingItemHBox;
    @FXML
    private HBox finishedItemHBox;

    private ItemTableView newItemTable;
    private ItemTableView onGoingItemTable;
    private ItemTableView finishedItemTable;

    @FXML
    protected void initialize() {
        rootVBox.getChildren().add(
                0, GlobalManager_Client.getStaffInfoPanel());
        newItemTable = new ItemTableView();
        newItemHBox.getChildren().add(0, newItemTable);
        initialTable(newItemTable);
        onGoingItemTable = new ItemTableView();
        onGoingItemHBox.getChildren().add(0, onGoingItemTable);
        initialTable(onGoingItemTable);
        finishedItemTable = new ItemTableView();
        finishedItemHBox.getChildren().add(0, finishedItemTable);
        initialTable(finishedItemTable);
    }

    private void initialTable(ItemTableView table) {
        HBox.setHgrow(table, javafx.scene.layout.Priority.ALWAYS);
        table.showColumns(
                NAME, TYPE, STATE, RETRIEVAL_CITY, EXPORT_CITY, IMPORT_CITY, DELIVERY_CITY);
    }

    @FXML
    protected void onAddItemCheck() {
        //TODO: add logic here
        onGoingItemTable.getItems().add(
                new ItemInfo(
                        "test",
                        "test",
                        1.0,
                        ItemState.Shipping,
                        new ItemInfo.RetrievalDeliveryInfo("c1", null),
                        new ItemInfo.RetrievalDeliveryInfo("c2", null),
                        new ItemInfo.ImportExportInfo("c3", null, 1.0),
                        new ItemInfo.ImportExportInfo("c4", null, 1.0)
                )
        );
    }
}
