package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.tables.*;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.interfaces.ItemInfo;
import main.interfaces.StaffInfo;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public class SUSTCManagerController extends ControllerBase {
    @FXML
    private VBox rootVBox;
    @FXML
    private HBox itemsHBox;
    @FXML
    private HBox staffsHBox;
    @FXML
    private HBox shipsHBox;
    @FXML
    private HBox containersHBox;
    @FXML
    private HBox citiesHBox;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab itemsTab;
    @FXML
    private Tab staffsTab;
    @FXML
    private Tab shipsTab;
    @FXML
    private Tab containersTab;
    @FXML
    private Tab citiesTab;
    @FXML
    private TextField searchItem;
    @FXML
    private TextField searchStaff;

    private ItemTableView itemTableView;
    private StaffTableView staffTableView;
    private ShipTableView shipTableView;
    private ContainerTableView containerTableView;
    private CityTableView cityTableView;

    private ObservableList<ItemInfo> itemInfos;
    private ObservableList<Map.Entry<Integer, StaffInfo>> staffInfos;

    @FXML
    private void initialize() {
        rootVBox.getChildren().add(
                0, GlobalManager_Client.getStaffInfoPanel());

        itemTableView = new ItemTableView();
        itemTableView.addItemBasicInfo();
        itemTableView.addRouteInfo();
        itemTableView.addStaffInfo();
        itemsHBox.getChildren().add(0, itemTableView);
        staffTableView = new StaffTableView();
        staffsHBox.getChildren().add(0, staffTableView);
        shipTableView = new ShipTableView();
        shipsHBox.getChildren().add(0, shipTableView);
        containerTableView = new ContainerTableView();
        containersHBox.getChildren().add(0, containerTableView);
        cityTableView = new CityTableView();
        citiesHBox.getChildren().add(0, cityTableView);
        initialTable(itemTableView);
        initialTable(staffTableView);
        initialTable(shipTableView);
        initialTable(containerTableView);
        initialTable(cityTableView);

        searchItem.textProperty().addListener((observable, oldValue, newValue) ->
                itemTableView.setItems(itemInfos.filtered(item -> item.name().contains(newValue))));
        searchStaff.textProperty().addListener((observable, oldValue, newValue) ->
                staffTableView.setItems(staffInfos.filtered(staff ->
                        staff.getValue().basicInfo().name().contains(newValue))));

        tabPane.getSelectionModel().select(itemsTab);
    }

    @FXML
    protected void onRefreshClick() {
        refreshTable(() -> {
            try {
                IServerProtocol server = ClientHelper.getConnection();

                Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();
                if (selectedItem.equals(itemsTab)) {
                    itemInfos.clear();
                    itemInfos.addAll(server.getAllItems().values());
                    itemTableView.setItems(itemInfos.filtered(item ->
                            item.name().contains(searchItem.getText())));
                } else if (selectedItem.equals(staffsTab)) {
                    staffInfos.clear();
                    staffInfos.addAll(server.getAllStaffs().entrySet());
                    staffTableView.setItems(staffInfos.filtered(staff ->
                            staff.getValue().basicInfo().name().contains(searchStaff.getText())));
                } else if (selectedItem.equals(shipsTab)) {
                    shipTableView.getItems().clear();
                    shipTableView.getItems().addAll(server.getAllShips().values());
                } else if (selectedItem.equals(containersTab)) {
                    containerTableView.getItems().clear();
                    containerTableView.getItems().addAll(server.getAllContainers().values());
                } else if (selectedItem.equals(citiesTab)) {
                    cityTableView.getItems().clear();
                    cityTableView.getItems().addAll(server.getAllCities().entrySet());
                }

            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
