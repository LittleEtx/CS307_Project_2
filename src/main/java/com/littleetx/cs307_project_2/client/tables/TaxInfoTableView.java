package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.database.database_type.CityInfo;
import com.littleetx.cs307_project_2.database.database_type.TaxInfo;
import javafx.beans.property.StringProperty;

import java.util.Map;

public class TaxInfoTableView extends TableViewBase<Map.Entry<TaxInfo.Key, TaxInfo.Value>> {
    public static final String CITY = "City";
    public static final String TYPE = "Type";
    public static final String IMPORT_TAX_RATE = "Import Tax Rate";
    public static final String EXPORT_TAX_RATE = "Export Tax Rate";
    private final Map<Integer, CityInfo> cityMap;


    public TaxInfoTableView(Map<Integer, CityInfo> cityMap) {
        super();
        this.cityMap = cityMap;
        addColumn(CITY, info -> this.cityMap.get(info.getKey().cityId()).name());
        addColumn(TYPE, info -> info.getKey().itemType());
        addColumn(IMPORT_TAX_RATE, info -> String.valueOf(info.getValue().import_rate));
        addColumn(EXPORT_TAX_RATE, info -> String.valueOf(info.getValue().export_rate));
    }

    @Override
    public void setFilter(StringProperty filterText) {
        setFilterText(filterText, info ->
                filter(cityMap.get(info.getKey().cityId()).name(), filterText.getValue())
                        || filter(info.getKey().itemType(), filterText.getValue())
        );
    }
}
