package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.ItemTableView;
import com.littleetx.cs307_project_2.client.tables.SeaportOfficerItemTableView;
import com.littleetx.cs307_project_2.client.tables.TaxInfoTableView;
import com.littleetx.cs307_project_2.database.user.SeaportOfficer;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.interfaces.ItemInfo;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static com.littleetx.cs307_project_2.client.tables.SeaportOfficerItemTableView.TableType.*;

public class SeaportOfficerController extends ControllerBase {
    @FXML
    private HBox importHBox;
    @FXML
    private HBox exportHBox;
    @FXML
    private HBox finishedHBox;
    @FXML
    private HBox taxHBox;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab importTab;
    @FXML
    private Tab exportTab;
    @FXML
    private Tab finishedTab;

    @FXML
    private Tab taxTab;

    @FXML
    private Node importPassBtn;
    @FXML
    private Node importRejectBtn;
    @FXML
    private Node exportPassBtn;
    @FXML
    private Node exportRejectBtn;

    @FXML
    private TextField searchImport;
    @FXML
    private TextField searchExport;
    @FXML
    private TextField searchFinished;

    private ItemTableView importTableView;
    private ItemTableView exportTableView;
    private ItemTableView finishedTableView;
    private TaxInfoTableView taxTableView;

    @FXML
    protected void initialize() {
        super.initialize();
        try {
            IServerProtocol server = ClientHelper.getConnection();

            importTableView = new SeaportOfficerItemTableView(IMPORT);
            importHBox.getChildren().add(0, importTableView);
            initialTable(importTableView);

            exportTableView = new SeaportOfficerItemTableView(EXPORT);
            exportHBox.getChildren().add(0, exportTableView);
            initialTable(exportTableView);

            finishedTableView = new SeaportOfficerItemTableView(FINISHED);
            finishedHBox.getChildren().add(0, finishedTableView);
            initialTable(finishedTableView);

            taxTableView = new TaxInfoTableView(server.getAllCities());
            taxHBox.getChildren().add(0, taxTableView);
            initialTable(taxTableView);

            exportPassBtn.setDisable(true);
            exportRejectBtn.setDisable(true);
            importPassBtn.setDisable(true);
            importRejectBtn.setDisable(true);

            exportTableView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        exportPassBtn.setDisable(newValue == null);
                        exportRejectBtn.setDisable(newValue == null);
                    });

            importTableView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        importPassBtn.setDisable(newValue == null);
                        importRejectBtn.setDisable(newValue == null);
                    });


            importTableView.setFilter(searchImport.textProperty());
            exportTableView.setFilter(searchExport.textProperty());
            finishedTableView.setFilter(searchFinished.textProperty());

            tabPane.getSelectionModel().select(importTab);
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            GlobalManager_Client.lostConnection();
        }
    }

    @FXML
    protected void onRefreshClick() {
        refreshTable(() -> {
            try {
                IServerProtocol server = ClientHelper.getConnection();
                var selected = tabPane.getSelectionModel().getSelectedItem();
                if (selected == importTab) {
                    importTableView.updateData(server.getItemsInPort(
                            GlobalManager_Client.getStaffID(), SeaportOfficer.GetItemType.IMPORT).values());
                } else if (selected == exportTab) {
                    exportTableView.updateData(server.getItemsInPort(
                            GlobalManager_Client.getStaffID(), SeaportOfficer.GetItemType.EXPORT).values());
                } else if (selected == finishedTab) {
                    finishedTableView.updateData(server.getItemsInPort(
                            GlobalManager_Client.getStaffID(), SeaportOfficer.GetItemType.FINISHED).values());
                } else if (selected == taxTab) {
                    var taxInfoMap = server.getTaxRates(GlobalManager_Client.getStaffInfo().city());
                    taxTableView.updateData(taxInfoMap.entrySet());
                }
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                GlobalManager_Client.lostConnection();
            }
        });
    }

    @FXML
    protected void onPassImportItemCheck() {
        checkItem(true, true);
    }

    @FXML
    protected void onRejectImportItemCheck() {
        checkItem(true, false);
    }

    @FXML
    protected void onPassExportItemCheck() {
        checkItem(false, true);
    }

    @FXML
    protected void onRejectExportItemCheck() {
        checkItem(false, false);
    }

    private void checkItem(boolean isImport, boolean isPass) {
        ItemInfo item = isImport ?
                importTableView.getSelectionModel().getSelectedItem() :
                exportTableView.getSelectionModel().getSelectedItem();
        GlobalManager_Client.showConfirm(
                "Are you sure to " + (isPass ? "pass " : "reject ") + (isImport ? "import" : "export") + " item: " + item.name() + "?",
                yes -> {
                    if (yes) {
                        try {
                            IServerProtocol server = ClientHelper.getConnection();
                            server.checkItem(GlobalManager_Client.getStaffID(), item.name(), isPass);
                            onRefreshClick();
                        } catch (MalformedURLException | NotBoundException | RemoteException e) {
                            GlobalManager_Client.lostConnection();
                        }
                    }
                });
    }

}
