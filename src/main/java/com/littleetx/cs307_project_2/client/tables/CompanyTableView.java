package com.littleetx.cs307_project_2.client.tables;

import javafx.beans.property.StringProperty;

import java.util.Map;

public class CompanyTableView extends TableViewBase<Map.Entry<Integer, String>> {
    public static final String ID = "Id";
    public static final String NAME = "Name";

    public CompanyTableView() {
        super();

        addColumn(ID, info -> info.getKey().toString());
        addColumn(NAME, Map.Entry::getValue);
    }

    @Override
    public void setFilter(StringProperty filterText) {
        setFilterText(filterText, info ->
                filter(info.getKey().toString(), filterText.getValue())
                        || filter(info.getValue(), filterText.getValue())
        );
    }
}
