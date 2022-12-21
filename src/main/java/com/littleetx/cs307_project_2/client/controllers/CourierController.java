package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.CourierItemTableView;
import com.littleetx.cs307_project_2.client.tables.ItemTableView;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.interfaces.ItemInfo;
import main.interfaces.ItemState;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CourierController extends ControllerBase {
    @FXML
    private VBox rootVBox;
    @FXML
    private HBox newItemHBox;
    @FXML
    private HBox onGoingItemHBox;
    @FXML
    private HBox finishedItemHBox;
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

    @FXML
    protected void onRefreshClick() {
        refreshTable(() -> {
            try {
                IServerProtocol server = ClientHelper.getConnection();
                int id = GlobalManager_Client.getStaffID();
                if (tabPane.getSelectionModel().getSelectedItem() == newItemTab) {
                    newItemTable.getItems().clear();
                    newItemTable.getItems().addAll(server.getCourierItems(id, Courier.GetItemType.New).values());
                    newItemTable.refresh();
                } else if (tabPane.getSelectionModel().getSelectedItem() == onGoingItemTab) {
                    onGoingItemTable.getItems().clear();
                    onGoingItemTable.getItems().addAll(server.getCourierItems(id, Courier.GetItemType.OnGoing).values());
                    onGoingItemTable.refresh();
                } else if (tabPane.getSelectionModel().getSelectedItem() == finishedItemTab) {
                    finishedItemTable.getItems().clear();
                    finishedItemTable.getItems().addAll(server.getCourierItems(id, Courier.GetItemType.Finished).values());
                    finishedItemTable.refresh();
                }
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                GlobalManager_Client.lostConnection();
            }
        });
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
