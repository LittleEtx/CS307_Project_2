package com.littleetx.cs307_project_2.client.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.function.Function;

public class TableViewBase<T> extends TableView<T> {

    public TableViewBase() {
        super();
        setPrefWidth(600);
        setTableMenuButtonVisible(true);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    }

    public void addColumn(String name, Function<T, String> extractor) {
        addColumn(name, extractor, false);
    }

    public void addColumn(String name, Function<T, String> extractor, boolean aliRight) {
        TableColumn<T, String> column = new TableColumn<>();
        column.setText(name);
        if (aliRight) {
            column.setStyle("-fx-alignment: CENTER-RIGHT;");
        } else {
            column.setStyle("-fx-alignment: CENTER-LEFT;");
        }
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        getColumns().add(column);
    }

    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols();

    static {
        symbols.setGroupingSeparator(' ');
    }

    public static String convertPrice(double price) {
        DecimalFormat df = new DecimalFormat(",###.00", symbols);
        return df.format(price);
    }

    public static String convertPrice(int price) {
        DecimalFormat df = new DecimalFormat(",###", symbols);
        return df.format(price);
    }

    public void showColumns(String... names) {
        getColumns().forEach(column -> {
            for (String name : names) {
                if (column.getText().equals(name)) {
                    column.setVisible(true);
                    return;
                }
            }
            column.setVisible(false);
        });
    }

    public void hideColumns(String... names) {
        getColumns().forEach(column -> {
            for (String name : names) {
                if (column.getText().equals(name)) {
                    column.setVisible(false);
                    return;
                }
            }
            column.setVisible(true);
        });
    }
}
