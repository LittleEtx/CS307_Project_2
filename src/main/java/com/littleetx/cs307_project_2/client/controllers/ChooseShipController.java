package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.ShipTableView;
import com.littleetx.cs307_project_2.database.DatabaseMapping;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.interfaces.ShipInfo;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChooseShipController extends ChooseControllerBase {

    @FXML
    private Text containerCode;
    @FXML
    private Text containerType;


    @FXML
    private TextField searchShip;
    private ShipTableView shipTableView;


    @FXML
    protected void initialize() {
        itemFullInfo = CompanyManagerController.selectedItem;
        shipTableView = new ShipTableView();
        initTable(shipTableView);
        shipTableView.setFilter(searchShip.textProperty());
        shipTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> chooseBtn.setDisable(newValue == null));
        initText();
        containerCode.setText(itemFullInfo.containerCode());
        containerType.setText(DatabaseMapping.getContainerTypeVisualStr(itemFullInfo.containerType()));
        onRefreshClick();
    }

    @FXML
    private void chooseShip() {
        ShipInfo shipInfo = shipTableView.getSelectionModel().getSelectedItem();
        try {
            IServerProtocol server = ClientHelper.getConnection();
            if (!server.loadContainerToShip(GlobalManager_Client.getStaffID(),
                    shipInfo.name(), itemFullInfo.containerCode())) {
                GlobalManager_Client.showAlert("Failed to load container to ship");
            } else {
                GlobalManager_Client.closeWindow();
            }
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }

    @FXML
    private void onRefreshClick() {
        try {
            IServerProtocol server = ClientHelper.getConnection();
            shipTableView.updateData(server.getCompanyShips(
                    GlobalManager_Client.getStaffID(), CompanyManager.GetShipType.DOCKED
            ).values());
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }
}
