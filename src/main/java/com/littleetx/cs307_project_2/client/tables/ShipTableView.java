package com.littleetx.cs307_project_2.client.tables;

import main.interfaces.ShipInfo;

public class ShipTableView extends TableViewBase<ShipInfo> {
    public static final String NAME = "Name";
    public static final String COMPANY = "Company";
    public static final String STATE = "State";

    public ShipTableView() {
        super();

        addColumn(NAME, ShipInfo::name);
        addColumn(COMPANY, ShipInfo::owner);
        addColumn(STATE, shipInfo -> shipInfo.sailing() ? "Sailing" : "Docked");
    }
}
