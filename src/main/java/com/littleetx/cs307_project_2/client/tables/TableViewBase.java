package com.littleetx.cs307_project_2.client.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.function.Function;

public class TableViewBase<T> extends TableView<T> {

    public TableViewBase() {
        super();
        setPrefWidth(600);
        setTableMenuButtonVisible(true);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    }

    protected void addColumn(String name, Function<T, String> extractor) {
        TableColumn<T, String> column = new TableColumn<>();
        column.setText(name);
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        getColumns().add(column);
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
}
