package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.ClientHelper;
import com.littleetx.cs307_project_2.client.GlobalManager_Client;
import com.littleetx.cs307_project_2.client.tables.ItemTableView;
import com.littleetx.cs307_project_2.client.tables.SeaportOfficerTableView;
import com.littleetx.cs307_project_2.client.tables.TaxInfoTableView;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import com.littleetx.cs307_project_2.database.user.SeaportOfficer;
import com.littleetx.cs307_project_2.server.IServerProtocol;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.interfaces.ItemInfo;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static com.littleetx.cs307_project_2.client.tables.SeaportOfficerTableView.TableType.*;

public class SeaportOfficerController extends ControllerBase {
    @FXML
    private VBox rootVBox;

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
    private Map<String, TaxInfo.Value> taxInfo;

    private ObservableList<ItemInfo> importList;
    private ObservableList<ItemInfo> exportList;
    private ObservableList<ItemInfo> finishedList;

    @FXML
    private void initialize() {
        rootVBox.getChildren().add(
                0, GlobalManager_Client.getStaffInfoPanel());
        try {
            taxInfo = new HashMap<>();
            IServerProtocol server = ClientHelper.getConnection();
            var taxInfoMap = server.getTaxRates(GlobalManager_Client.getStaffInfo().city());
            updateTaxInfo(taxInfoMap);

            importTableView = new SeaportOfficerTableView(IMPORT, taxInfo);
            importHBox.getChildren().add(0, importTableView);
            initialTable(importTableView);

            exportTableView = new SeaportOfficerTableView(EXPORT, taxInfo);
            exportHBox.getChildren().add(0, exportTableView);
            initialTable(exportTableView);

            finishedTableView = new SeaportOfficerTableView(FINISHED, taxInfo);
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
            importList = importTableView.getItems();
            exportList = exportTableView.getItems();
            finishedList = finishedTableView.getItems();

            searchImport.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    importTableView.setItems(importList);
                } else {
                    importTableView.setItems(importList.filtered(item -> item.name().contains(newValue)));
                }
            });

            searchExport.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    exportTableView.setItems(exportList);
                } else {
                    exportTableView.setItems(exportList.filtered(item -> item.name().contains(newValue)));
                }
            });

            searchFinished.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    finishedTableView.setItems(finishedList);
                } else {
                    finishedTableView.setItems(finishedList.filtered(item -> item.name().contains(newValue)));
                }
            });

            tabPane.getSelectionModel().select(importTab);
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();
            GlobalManager_Client.lostConnection();
        }
    }

    private void updateTaxInfo(Map<TaxInfo.Key, TaxInfo.Value> taxInfoMap) {
        taxInfo.clear();
        for (var entry : taxInfoMap.entrySet()) {
            taxInfo.put(entry.getKey().itemType(), entry.getValue());
        }
    }

    @FXML
    protected void onRefreshClick() {
        refreshTable(() -> {
            try {
                IServerProtocol server = ClientHelper.getConnection();
                var selected = tabPane.getSelectionModel().getSelectedItem();
                if (selected == importTab) {
                    importList.setAll(server.getItemsInPort(
                            GlobalManager_Client.getStaffID(), SeaportOfficer.GetItemType.IMPORT).values());
                    importTableView.setItems(importList.filtered(item -> item.name().contains(searchImport.getText())));
                } else if (selected == exportTab) {
                    exportList.setAll(server.getItemsInPort(
                            GlobalManager_Client.getStaffID(), SeaportOfficer.GetItemType.EXPORT).values());
                    exportTableView.setItems(exportList.filtered(item -> item.name().contains(searchExport.getText())));
                } else if (selected == finishedTab) {
                    finishedList.setAll(server.getItemsInPort(
                            GlobalManager_Client.getStaffID(), SeaportOfficer.GetItemType.FINISHED).values());
                    finishedTableView.setItems(finishedList.filtered(item -> item.name().contains(searchFinished.getText())));
                } else if (selected == taxTab) {
                    var taxInfoMap = server.getTaxRates(GlobalManager_Client.getStaffInfo().city());
                    taxTableView.getItems().setAll(taxInfoMap.entrySet());
                    updateTaxInfo(taxInfoMap);
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
                "Are you sure to pass " + (isImport ? "import" : "export" + " item: ") + item.name() + "?",
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
