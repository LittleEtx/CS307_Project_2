package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.*;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.interfaces.ContainerInfo;
import main.interfaces.ItemInfo;
import main.interfaces.ShipInfo;
import main.interfaces.StaffInfo;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import static com.littleetx.cs307_project_2.client.GlobalManager_Client.getStaffID;

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
    private HBox companiesHBox;

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
    private Tab companiesTab;
    @FXML
    private TextField searchItemName;

    @FXML
    private TextField searchStaff;
    @FXML
    private TextField searchShipName;
    @FXML
    private TextField searchContainerCode;

    private ItemTableView itemTableView;
    private StaffTableView staffTableView;
    private ShipTableView shipTableView;
    private ContainerTableView containerTableView;
    private CityTableView cityTableView;
    private CompanyTableView companyTableView;

    private ObservableList<ItemInfo> itemInfos;
    private ObservableList<Map.Entry<Integer, StaffInfo>> staffInfos;
    private ObservableList<ShipInfo> shipInfos;
    private ObservableList<ContainerInfo> containerInfos;

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
        companyTableView = new CompanyTableView();
        companiesHBox.getChildren().add(0, companyTableView);

        initialTable(itemTableView);
        initialTable(staffTableView);
        initialTable(shipTableView);
        initialTable(containerTableView);
        initialTable(cityTableView);
        initialTable(companyTableView);

        itemInfos = itemTableView.getItems();
        staffInfos = staffTableView.getItems();
        shipInfos = shipTableView.getItems();
        containerInfos = containerTableView.getItems();

        searchItemName.textProperty().addListener((observable, oldValue, newValue) ->
                itemTableView.setItems(itemInfos.filtered(item -> item.name().contains(newValue))));
        searchStaff.textProperty().addListener((observable, oldValue, newValue) ->
                staffTableView.setItems(staffInfos
                        .filtered(staff -> staff.getValue().basicInfo().name().contains(newValue)
                                || staff.getKey().toString().contains(newValue))));
        searchShipName.textProperty().addListener((observable, oldValue, newValue) ->
                shipTableView.setItems(
                        shipInfos.filtered(ship -> ship.name().contains(newValue))
                ));

        searchContainerCode.textProperty().addListener((observable, oldValue, newValue) ->
                containerTableView.setItems(
                        containerInfos.filtered(con -> con.code().contains(newValue))
                ));

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
                    itemInfos.addAll(server.getAllItems(getStaffID()).values());
                    itemTableView.setItems(itemInfos
                            .filtered(item -> item.name().contains(searchItemName.getText())));
                } else if (selectedItem.equals(staffsTab)) {
                    staffInfos.clear();
                    staffInfos.addAll(server.getAllStaffs(getStaffID()).entrySet());
                    staffTableView.setItems(staffInfos.filtered(staff ->
                            staff.getValue().basicInfo().name().contains(searchStaff.getText())
                                    || staff.getKey().toString().contains(searchStaff.toString())));
                } else if (selectedItem.equals(shipsTab)) {
                    shipInfos.clear();
                    shipInfos.addAll(server.getAllShips(getStaffID()).values());
                    shipTableView.setItems(
                            shipInfos.filtered(ship -> ship.name().contains(searchShipName.getText()))
                    );
                } else if (selectedItem.equals(containersTab)) {
                    containerInfos.clear();
                    containerInfos.addAll(server.getAllContainers(GlobalManager_Client.getStaffID()).values());
                    containerTableView.setItems(
                            containerInfos.filtered(con -> con.code().contains(searchContainerCode.getText()))
                    );
                } else if (selectedItem.equals(citiesTab)) {
                    cityTableView.getItems().clear();
                    cityTableView.getItems().addAll(server.getAllCities().entrySet());
                } else if (selectedItem.equals(companiesTab)) {
                    companyTableView.getItems().clear();
                    companyTableView.getItems().addAll(server.getAllCompanies().entrySet());
                }

            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
