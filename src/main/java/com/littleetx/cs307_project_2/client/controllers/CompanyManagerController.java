package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.CompanyManagerItemTableView;
import com.littleetx.cs307_project_2.client.tables.ShipTableView;
import com.littleetx.cs307_project_2.client.tables.StaffTableView;
import com.littleetx.cs307_project_2.client.tables.TaxInfoTableView;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static com.littleetx.cs307_project_2.client.GlobalManager_Client.getStaffID;

public class CompanyManagerController extends ControllerBase {

    @FXML
    private HBox exportHBox;
    @FXML
    private HBox onShipHBox;
    @FXML
    private HBox importHBox;
    @FXML
    private HBox allItemsHBox;
    @FXML
    private HBox couriersHBox;
    @FXML
    private HBox shipsHBox;
    @FXML
    private HBox taxHBox;
    @FXML
    private Tab exportTab;
    @FXML
    private Tab onShipTab;
    @FXML
    private Tab importTab;
    @FXML
    private Tab allItemsTab;
    @FXML
    private Tab couriersTab;
    @FXML
    private Tab shipsTab;
    @FXML
    private Tab taxTab;

    @FXML
    private TextField searchExport;
    @FXML
    private TextField searchOnShip;
    @FXML
    private TextField searchImport;
    @FXML
    private TextField searchAllItems;
    @FXML
    private TextField searchCouriers;
    @FXML
    private TextField searchShips;

    @FXML
    private Node packToContainerBtn;
    @FXML
    private Node loadToShipBtn;
    @FXML
    private Node startSailingBtn;
    @FXML
    private Node unloadFromShipBtn;
    @FXML
    private Node sendToCheckBtn;

    private CompanyManagerItemTableView exportTableView;
    private CompanyManagerItemTableView onShipTableView;
    private CompanyManagerItemTableView importTableView;
    private CompanyManagerItemTableView allItemTableView;
    private StaffTableView courierTableView;
    private ShipTableView shipTableView;
    private TaxInfoTableView taxInfoTableView;

    @FXML
    protected void initialize() {
        super.initialize();
        exportTableView = new CompanyManagerItemTableView();
        exportTableView.showColumn(CompanyManagerItemTableView.ShowType.EXPORT);
        exportHBox.getChildren().add(0, exportTableView);
        initialTable(exportTableView);

        onShipTableView = new CompanyManagerItemTableView();
        onShipTableView.showColumn(CompanyManagerItemTableView.ShowType.ON_SHIP);
        onShipHBox.getChildren().add(0, onShipTableView);
        initialTable(onShipTableView);

        importTableView = new CompanyManagerItemTableView();
        importTableView.showColumn(CompanyManagerItemTableView.ShowType.IMPORT);
        importHBox.getChildren().add(0, importTableView);
        initialTable(onShipTableView);

        allItemTableView = new CompanyManagerItemTableView();
        allItemsHBox.getChildren().add(0, allItemTableView);
        initialTable(allItemTableView);

        courierTableView = new StaffTableView();
        couriersHBox.getChildren().add(0, courierTableView);
        initialTable(courierTableView);

        shipTableView = new ShipTableView();
        shipsHBox.getChildren().add(0, shipTableView);
        initialTable(shipTableView);
        try {
            IServerProtocol server = ClientHelper.getConnection();

            taxInfoTableView = new TaxInfoTableView(server.getAllCities());
            taxHBox.getChildren().add(0, taxInfoTableView);
            initialTable(taxInfoTableView);
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
        exportTableView.setFilter(searchExport.textProperty());
        onShipTableView.setFilter(searchOnShip.textProperty());
        importTableView.setFilter(searchImport.textProperty());
        allItemTableView.setFilter(searchAllItems.textProperty());
        courierTableView.setFilter(searchCouriers.textProperty());
        shipTableView.setFilter(searchShips.textProperty());
    }


    @FXML
    private void onRefreshClick() {
        refreshTable(() -> {
            try {
                IServerProtocol server = ClientHelper.getConnection();
                Tab current = tabPane.getSelectionModel().getSelectedItem();
                int id = getStaffID();
                if (current == exportTab) {
                    exportTableView.updateData(server.getCompanyItems(id,
                            CompanyManager.GetItemType.EXPORT).values());
                } else if (current == onShipTab) {
                    onShipTableView.updateData(server.getCompanyItems(id,
                            CompanyManager.GetItemType.ON_SHIP).values());
                } else if (current == importTab) {
                    importTableView.updateData(server.getCompanyItems(id,
                            CompanyManager.GetItemType.IMPORT).values());
                } else if (current == allItemsTab) {
                    allItemTableView.updateData(server.getCompanyItems(id,
                            CompanyManager.GetItemType.ALL).values());
                } else if (current == couriersTab) {
                    courierTableView.updateData(server.getCompanyCouriers(getStaffID()).entrySet());
                } else if (current == shipsTab) {
                    shipTableView.updateData(server.getCompanyShips(getStaffID(),
                            CompanyManager.GetShipType.ALL).values());
                } else if (current == taxTab) {
                    taxInfoTableView.updateData(server.getTaxRates().entrySet());
                }
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                GlobalManager_Client.lostConnection();
            }
        });
    }

    @FXML
    private void onPackToContainerCheck() {
        //TODO
    }

    @FXML
    private void onLoadToShipCheck() {
        //TODO
    }

    @FXML
    private void onStartSailingCheck() {
        //TODO
    }

    @FXML
    private void onUnloadItemCheck() {
        //TODO
    }

    @FXML
    private void onSendToCheckCheck() {
        //TODO
    }
}
