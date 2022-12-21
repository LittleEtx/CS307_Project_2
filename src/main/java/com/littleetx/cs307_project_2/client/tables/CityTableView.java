package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.database.database_type.CityInfo;

import java.util.Map;

public class CityTableView extends TableViewBase<Map.Entry<Integer, CityInfo>> {
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String TYPE = "Type";

    public CityTableView() {
        super();

        addColumn(ID, entry -> entry.getKey().toString());
        addColumn(NAME, info -> info.getValue().name());
        addColumn(TYPE, info -> info.getValue().isSeaport() ? "Seaport" : "Inland");
    }

}
