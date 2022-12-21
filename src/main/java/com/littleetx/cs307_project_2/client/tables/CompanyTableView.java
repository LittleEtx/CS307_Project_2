package com.littleetx.cs307_project_2.client.tables;

import java.util.Map;

public class CompanyTableView extends TableViewBase<Map.Entry<Integer, String>> {
    public static final String ID = "Id";
    public static final String NAME = "Name";

    public CompanyTableView() {
        super();

        addColumn(ID, info -> info.getKey().toString());
        addColumn(NAME, Map.Entry::getValue);
    }
}
