package com.littleetx.cs307_project_2.client.tables;

import cs307.project2.interfaces.ItemInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.function.Function;

public class ItemTableView extends TableView<ItemInfo> {
    public static final String NAME = "Name";
    public static final String TYPE = "Type";
    public static final String PRICE = "Price";
    public static final String STATE = "State";
    public static final String RETRIEVAL_CITY = "Retrieval City";
    public static final String RETRIEVAL_COURIER = "Retrieval Courier";
    public static final String EXPORT_CITY = "Export City";
    public static final String EXPORT_TAX = "Export Tax";
    public static final String EXPORT_OFFICER = "Export Officer";
    public static final String IMPORT_CITY = "Import City";
    public static final String IMPORT_TAX = "Import Tax";
    public static final String IMPORT_OFFICER = "Import Officer";
    public static final String DELIVERY_CITY = "Delivery City";
    public static final String DELIVERY_COURIER = "Delivery Courier";


    public ItemTableView() {
        super();
        setPrefWidth(600);
        setTableMenuButtonVisible(true);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        addColumn(NAME, ItemInfo::name);
        addColumn(TYPE, ItemInfo::$class);
        addColumn(PRICE, info -> String.valueOf(info.price()));
        addColumn(STATE, info -> info.state().toString());
        addColumn(RETRIEVAL_CITY, info -> info.retrieval().city());
        addColumn(RETRIEVAL_COURIER, info -> info.retrieval().courier());
        addColumn(EXPORT_CITY, info -> info.export().city());
        addColumn(EXPORT_TAX, info -> String.valueOf(info.export().tax()));
        addColumn(EXPORT_OFFICER, info -> info.export().officer());
        addColumn(IMPORT_CITY, info -> info.$import().city());
        addColumn(IMPORT_TAX, info -> String.valueOf(info.$import().tax()));
        addColumn(IMPORT_OFFICER, info -> info.$import().officer());
        addColumn(DELIVERY_CITY, info -> info.delivery().city());
        addColumn(DELIVERY_COURIER, info -> info.delivery().courier());
    }

    public void showColumns(String... columns) {
        getColumns().forEach(column -> column.setVisible(false));
        for (String column : columns) {
            getColumns().stream()
                    .filter(c -> c.getText().equals(column))
                    .findFirst()
                    .ifPresent(c -> c.setVisible(true));
        }
    }

    private void addColumn(String name, Function<ItemInfo, String> extractor) {
        TableColumn<ItemInfo, String> column = new TableColumn<>();
        column.setText(name);
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        getColumns().add(column);
    }
}
