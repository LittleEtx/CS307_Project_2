package com.littleetx.cs307_project_2.client.controllers;

import com.littleetx.cs307_project_2.client.tables.TableViewBase;
import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ChooseControllerBase {
    @FXML
    protected HBox tableHBox;
    @FXML
    protected Node chooseBtn;
    @FXML
    protected Text itemName;
    @FXML
    protected Text itemType;
    @FXML
    protected Text itemPrice;
    @FXML
    protected Text exportCity;
    @FXML
    protected Text exportTax;
    protected ItemFullInfo itemFullInfo;

    protected void initText() {
        itemName.setText(itemFullInfo.itemInfo().name());
        itemType.setText(itemFullInfo.itemInfo().$class());
        itemPrice.setText(TableViewBase.convertPrice((int) itemFullInfo.itemInfo().price()));
        exportCity.setText(itemFullInfo.itemInfo().export().city());
        exportTax.setText(TableViewBase.convertPrice(itemFullInfo.itemInfo().export().tax()));
    }

    protected void initTable(TableView<?> tableView) {
        tableHBox.getChildren().add(0, tableView);
        HBox.setHgrow(tableView, javafx.scene.layout.Priority.ALWAYS);
    }

}
