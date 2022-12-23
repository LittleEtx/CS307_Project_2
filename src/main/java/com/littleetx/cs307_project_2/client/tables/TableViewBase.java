package com.littleetx.cs307_project_2.client.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class TableViewBase<T> extends TableView<T> {

    private final ObservableList<T> data;
    private Predicate<T> filterPredicate;

    public TableViewBase() {
        super();
        setPrefWidth(600);
        setTableMenuButtonVisible(true);
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        data = getItems();
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

    public void updateData(Collection<T> data) {
        this.data.setAll(data);
        if (filterPredicate != null) {
            setItems(this.data.filtered(item -> filterPredicate.test(item)));
        } else {
            setItems(this.data);
        }
    }

    protected void setFilterText(StringProperty filterText, Predicate<T> filter) {
        filterPredicate = filter;
        filterText.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                setItems(data);
            } else {
                setItems(data.filtered(filter));
            }
        });
    }

    abstract public void setFilter(StringProperty filterText);

    protected boolean filter(String text, String keyWord) {
        return text != null && text.toLowerCase().contains(keyWord.toLowerCase());
    }
}
