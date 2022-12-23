package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.ContainerTableView;
import com.littleetx.cs307_project_2.database.user.CompanyManager;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import main.interfaces.ContainerInfo;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChooseContainerController extends ChooseControllerBase {
    @FXML
    private TextField searchContainer;
    @FXML
    private Node chooseBtn;
    private ContainerTableView containerTableView;


    @FXML
    protected void initialize() {
        containerTableView = new ContainerTableView();
        initTable(containerTableView);
        containerTableView.setFilter(searchContainer.textProperty());

        containerTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> chooseBtn.setDisable(newValue == null));

        itemFullInfo = CompanyManagerController.selectedItem;
        initText();
        onRefreshClick();
    }

    @FXML
    private void chooseContainer() {
        ContainerInfo containerInfo = containerTableView.getSelectionModel().getSelectedItem();
        try {
            IServerProtocol server = ClientHelper.getConnection();
            if (!server.loadItemToContainer(GlobalManager_Client.getStaffID(),
                    itemFullInfo.itemInfo().name(), containerInfo.code())) {
                GlobalManager_Client.showAlert("Failed to load item to container");
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
            containerTableView.updateData(server.getContainers(
                    GlobalManager_Client.getStaffID(), CompanyManager.GetContainerType.IDLE
            ).values());
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }
}
