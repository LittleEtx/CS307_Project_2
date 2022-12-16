package com.littleetx.cs307_project_2.client;

import com.littleetx.cs307_project_2.client.tables.CourierItemTableView;
import com.littleetx.cs307_project_2.client.tables.ItemTableView;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import cs307.project2.interfaces.ItemInfo;
import cs307.project2.interfaces.ItemState;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CourierController {
    @FXML
    private VBox rootVBox;
    @FXML
    private HBox newItemHBox;
    @FXML
    private HBox onGoingItemHBox;
    @FXML
    private HBox finishedItemHBox;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab newItemTab;
    @FXML
    private Tab onGoingItemTab;
    @FXML
    private Tab finishedItemTab;

    private ItemTableView newItemTable;
    private ItemTableView onGoingItemTable;
    private ItemTableView finishedItemTable;

    @FXML
    protected void initialize() {
        rootVBox.getChildren().add(
                0, GlobalManager_Client.getStaffInfoPanel());
        newItemTable = new CourierItemTableView(null);
        newItemHBox.getChildren().add(0, newItemTable);
        initialTable(newItemTable);
        String name = GlobalManager_Client.getStaffInfo().basicInfo().name();
        onGoingItemTable = new CourierItemTableView(name);
        onGoingItemHBox.getChildren().add(0, onGoingItemTable);
        initialTable(onGoingItemTable);
        finishedItemTable = new CourierItemTableView(name);
        finishedItemHBox.getChildren().add(0, finishedItemTable);
        initialTable(finishedItemTable);

        tabPane.getSelectionModel().select(onGoingItemTab);
    }

    private void initialTable(ItemTableView table) {
        HBox.setHgrow(table, javafx.scene.layout.Priority.ALWAYS);
    }

    @FXML
    protected void refreshTable() {
        if (newItemTable == null)
            return;
        try {
            IServerProtocol server = ClientHelper.getConnection();
            int id = GlobalManager_Client.getStaffID();
            if (tabPane.getSelectionModel().getSelectedItem() == newItemTab) {
                PauseTransition pause = new PauseTransition(Duration.seconds(0.01));
                pause.setOnFinished(event -> {
                    try {
                        if (tabPane.getSelectionModel().getSelectedItem() == newItemTab) {
                            newItemTable.getItems().clear();
                            newItemTable.getItems().addAll(server.getCourierItems(id, Courier.GetItemType.New).values());
                            newItemTable.refresh();
                        }
                    } catch (RemoteException e) {
                        GlobalManager_Client.lostConnection();
                    }
                });
                pause.play();
            } else if (tabPane.getSelectionModel().getSelectedItem() == onGoingItemTab) {
                PauseTransition pause = new PauseTransition(Duration.seconds(0.01));
                pause.setOnFinished(event -> {
                    try {
                        if (tabPane.getSelectionModel().getSelectedItem() == onGoingItemTab) {
                            onGoingItemTable.getItems().clear();
                            onGoingItemTable.getItems().addAll(server.getCourierItems(id, Courier.GetItemType.OnGoing).values());
                            onGoingItemTable.refresh();
                        }
                    } catch (RemoteException e) {
                        GlobalManager_Client.lostConnection();
                    }
                });
                pause.play();
            } else if (tabPane.getSelectionModel().getSelectedItem() == finishedItemTab) {
                PauseTransition pause = new PauseTransition(Duration.seconds(0.01));
                pause.setOnFinished(event -> {
                    try {
                        if (tabPane.getSelectionModel().getSelectedItem() == finishedItemTab) {
                            finishedItemTable.getItems().clear();
                            finishedItemTable.getItems().addAll(server.getCourierItems(id, Courier.GetItemType.Finished).values());
                            finishedItemTable.refresh();
                        }
                    } catch (RemoteException e) {
                        GlobalManager_Client.lostConnection();
                    }
                });
                pause.play();
            }
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }

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
