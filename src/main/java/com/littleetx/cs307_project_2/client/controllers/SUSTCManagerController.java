package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.*;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static com.littleetx.cs307_project_2.client.GlobalManager_Client.getStaffID;

public class SUSTCManagerController extends ControllerBase {
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

    @FXML
    protected void initialize() {
        super.initialize();
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

        itemTableView.setFilter(searchItemName.textProperty());
        staffTableView.setFilter(searchStaff.textProperty());
        shipTableView.setFilter(searchShipName.textProperty());
        containerTableView.setFilter(searchContainerCode.textProperty());

        tabPane.getSelectionModel().select(itemsTab);
    }

    @FXML
    protected void onRefreshClick() {
        refreshTable(() -> {
            try {
                IServerProtocol server = ClientHelper.getConnection();

                Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();
                if (selectedItem.equals(itemsTab)) {
                    itemTableView.updateData(server.getAllItems(getStaffID()).values());
                } else if (selectedItem.equals(staffsTab)) {
                    staffTableView.updateData(server.getAllStaffs(getStaffID()).entrySet());
                } else if (selectedItem.equals(shipsTab)) {
                    shipTableView.updateData(server.getAllShips(getStaffID()).values());
                } else if (selectedItem.equals(containersTab)) {
                    containerTableView.updateData(server.getAllContainers(GlobalManager_Client.getStaffID()).values());
                } else if (selectedItem.equals(citiesTab)) {
                    cityTableView.updateData(server.getAllCities().entrySet());
                } else if (selectedItem.equals(companiesTab)) {
                    companyTableView.updateData(server.getAllCompanies().entrySet());
                }

            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                GlobalManager_Client.lostConnection();
            }
        });
    }

}
