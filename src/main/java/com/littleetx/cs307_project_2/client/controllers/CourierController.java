package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.CourierItemTableView;
import com.littleetx.cs307_project_2.client.tables.ItemTableView;
import com.littleetx.cs307_project_2.database.user.Courier;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import main.interfaces.ItemInfo;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static com.littleetx.cs307_project_2.client.GlobalManager_Client.getStaffID;
import static com.littleetx.cs307_project_2.client.GlobalManager_Client.showConfirm;

public class CourierController extends ControllerBase {
    public static final String NEW_ITEM_FXML = "NewItem.fxml";
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

    @FXML
    private Node takeItemNode;
    @FXML
    private Node updateItemNode;

    @FXML
    private TextField searchNewItem;
    @FXML
    private TextField searchOnGoingItem;
    @FXML
    private TextField searchFinishedItem;

    private ItemTableView newItemTable;
    private ItemTableView onGoingItemTable;
    private ItemTableView finishedItemTable;

    @FXML
    protected void initialize() {
        super.initialize();
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
        takeItemNode.setDisable(true);
        updateItemNode.setDisable(true);
        newItemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> takeItemNode.setDisable(newValue == null));
        onGoingItemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateItemNode.setDisable(newValue == null));

        newItemTable.setFilter(searchNewItem.textProperty());
        onGoingItemTable.setFilter(searchOnGoingItem.textProperty());
        finishedItemTable.setFilter(searchFinishedItem.textProperty());

        tabPane.getSelectionModel().select(onGoingItemTab);
    }

    @FXML
    protected void onRefreshClick() {
        refreshTable(() -> {
            try {
                IServerProtocol server = ClientHelper.getConnection();
                int id = getStaffID();
                if (tabPane.getSelectionModel().getSelectedItem() == newItemTab) {
                    newItemTable.updateData(server.getCourierItems(id, Courier.GetItemType.New).values());
                } else if (tabPane.getSelectionModel().getSelectedItem() == onGoingItemTab) {
                    onGoingItemTable.updateData(server.getCourierItems(id, Courier.GetItemType.OnGoing).values());
                } else if (tabPane.getSelectionModel().getSelectedItem() == finishedItemTab) {
                    finishedItemTable.updateData(server.getCourierItems(id, Courier.GetItemType.Finished).values());
                }
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                GlobalManager_Client.lostConnection();
            }
        });
    }

    @FXML
    protected void onAddItemCheck() {
        GlobalManager_Client.showWindow("New Item", NEW_ITEM_FXML, 600, 250);
    }

    @FXML
    protected void onTakeItemCheck() {
        ItemInfo item = newItemTable.getSelectionModel().getSelectedItem();
        showConfirm("Are you sure to take item " + item.name() + "?", yes -> {
            if (!yes) {
                return;
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
            tabPane.setDisable(true);
            pause.setOnFinished(event -> {
                try {
                    IServerProtocol server = ClientHelper.getConnection();
                    if (!server.takeItem(getStaffID(), item.name())) {
                        GlobalManager_Client.showAlert("Take Item Failed!");
                        tabPane.setDisable(false);
                    } else {
                        onRefreshClick();
                    }
                } catch (MalformedURLException | NotBoundException | RemoteException e) {
                    GlobalManager_Client.lostConnection();
                }
            });
            pause.play();
        });
    }

    @FXML
    protected void onUpdateItemCheck() {
        ItemInfo item = onGoingItemTable.getSelectionModel().getSelectedItem();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        tabPane.setDisable(true);
        pause.setOnFinished(event -> {
            try {
                var server = ClientHelper.getConnection();
                if (!server.updateItemState(getStaffID(), item.name())) {
                    GlobalManager_Client.showAlert("Update Failed!");
                    tabPane.setDisable(false);
                } else {
                    onRefreshClick();
                }
            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                GlobalManager_Client.lostConnection();
            }
        });
        pause.play();
    }
}
