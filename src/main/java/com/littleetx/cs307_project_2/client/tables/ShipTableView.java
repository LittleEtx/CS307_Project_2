package com.littleetx.cs307_project_2.client.tables;

import javafx.beans.property.StringProperty;
import main.interfaces.ShipInfo;

public class ShipTableView extends TableViewBase<ShipInfo> {
    public static final String SHIP_NAME = "Ship Name";
    public static final String SHIP_COMPANY = "Company";
    public static final String SHIP_STATE = "State";

    public ShipTableView() {
        super();

        addColumn(SHIP_NAME, ShipInfo::name);
        addColumn(SHIP_COMPANY, ShipInfo::owner);
        addColumn(SHIP_STATE, shipInfo -> shipInfo.sailing() ? "Sailing" : "Docked");
    }

    @Override
    public void setFilter(StringProperty filterText) {
        setFilterText(filterText, info ->
                filter(info.name(), filterText.getValue())
                        || filter(info.owner(), filterText.getValue())
                        || filter(info.sailing() ? "Sailing" : "Docked", filterText.getValue())
        );
    }
}
