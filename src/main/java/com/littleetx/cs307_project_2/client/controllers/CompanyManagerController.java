package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.FullItemTableView;
import com.littleetx.cs307_project_2.client.tables.ShipTableView;
import com.littleetx.cs307_project_2.client.tables.StaffTableView;
import com.littleetx.cs307_project_2.client.tables.TaxInfoTableView;
import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.interfaces.ItemState;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static com.littleetx.cs307_project_2.client.GlobalManager_Client.getStaffID;
import static com.littleetx.cs307_project_2.client.GlobalManager_Client.showConfirm;

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

    private FullItemTableView exportTableView;
    private FullItemTableView onShipTableView;
    private FullItemTableView importTableView;
    private FullItemTableView allItemTableView;
    private StaffTableView courierTableView;
    private ShipTableView shipTableView;
    private TaxInfoTableView taxInfoTableView;

    public static ItemFullInfo selectedItem;
    public static final String PACK_TO_CONTAINER_FXML = "ChooseContainer.fxml";
    public static final String LOAD_TO_SHIP_FXML = "ChooseShip.fxml";

    @FXML
    protected void initialize() {
        super.initialize();
        exportTableView = new FullItemTableView();
        exportTableView.showColumn(FullItemTableView.ShowType.EXPORT);
        exportHBox.getChildren().add(0, exportTableView);
        initialTable(exportTableView);

        onShipTableView = new FullItemTableView();
        onShipTableView.showColumn(FullItemTableView.ShowType.ON_SHIP);
        onShipHBox.getChildren().add(0, onShipTableView);
        initialTable(onShipTableView);

        importTableView = new FullItemTableView();
        importTableView.showColumn(FullItemTableView.ShowType.IMPORT);
        importHBox.getChildren().add(0, importTableView);
        initialTable(importTableView);

        allItemTableView = new FullItemTableView();
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

        packToContainerBtn.setDisable(true);
        loadToShipBtn.setDisable(true);
        startSailingBtn.setDisable(true);
        unloadFromShipBtn.setDisable(true);
        sendToCheckBtn.setDisable(true);

        exportTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    packToContainerBtn.setDisable(newValue == null);
                    loadToShipBtn.setDisable(newValue == null ||
                            newValue.containerCode() == null);
                    selectedItem = newValue;
                });

        onShipTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    startSailingBtn.setDisable(newValue == null ||
                            newValue.itemInfo().state() != ItemState.WaitingForShipping);
                    unloadFromShipBtn.setDisable(newValue == null ||
                            newValue.itemInfo().state() != ItemState.Shipping);
                });

        importTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> sendToCheckBtn.setDisable(newValue == null));
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
        ItemFullInfo item = exportTableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        GlobalManager_Client.showWindow("Pack To Container",
                PACK_TO_CONTAINER_FXML, 600, 500, this::onRefreshClick);
    }


    @FXML
    private void onLoadToShipCheck() {
        ItemFullInfo item = exportTableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        GlobalManager_Client.showWindow("Load To Ship",
                LOAD_TO_SHIP_FXML, 600, 500, this::onRefreshClick);
    }

    @FXML
    private void onStartSailingCheck() {
        ItemFullInfo item = onShipTableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        showConfirm("Are you sure to start sailing "
                + item.shipName() + "?", yes -> {
            if (yes) {
                try {
                    IServerProtocol server = ClientHelper.getConnection();
                    if (!server.shipStartSailing(GlobalManager_Client.getStaffID(), item.shipName())) {
                        GlobalManager_Client.showAlert("Error: Cannot start sailing");
                    }
                    onRefreshClick();
                } catch (MalformedURLException | NotBoundException | RemoteException e) {
                    GlobalManager_Client.lostConnection();
                }
            }
        });

    }

    @FXML
    private void onUnloadItemCheck() {
        ItemFullInfo item = onShipTableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        try {
            IServerProtocol server = ClientHelper.getConnection();
            if (!server.unloadItem(GlobalManager_Client.getStaffID(), item.itemInfo().name())) {
                GlobalManager_Client.showAlert("Error: Cannot unload item");
            }
            onRefreshClick();
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }

    @FXML
    private void onSendToCheckCheck() {
        ItemFullInfo item = importTableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        try {
            IServerProtocol server = ClientHelper.getConnection();
            if (!server.itemWaitForChecking(GlobalManager_Client.getStaffID(), item.itemInfo().name())) {
                GlobalManager_Client.showAlert("Error: Cannot send item to check");
            }
            onRefreshClick();
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }
}
