package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import main.interfaces.ItemInfo;

public class ItemTableView extends TableViewBase<ItemInfo> {
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
    }

    public void addItemBasicInfo() {
        addColumn(NAME, ItemInfo::name);
        addColumn(TYPE, ItemInfo::$class);
        addColumn(PRICE, itemInfo -> String.valueOf(itemInfo.price()));
        addColumn(STATE, itemInfo -> DatabaseMapping.getStateVisualString(itemInfo.state()));
    }

    public void addRouteInfo() {
        addColumn(RETRIEVAL_CITY, itemInfo -> itemInfo.retrieval().city());
        addColumn(EXPORT_CITY, itemInfo -> itemInfo.export().city());
        addColumn(IMPORT_CITY, itemInfo -> itemInfo.$import().city());
        addColumn(DELIVERY_CITY, itemInfo -> itemInfo.delivery().city());
    }

    public void addStaffInfo() {
        addColumn(RETRIEVAL_COURIER, itemInfo -> itemInfo.retrieval().courier());
        addColumn(EXPORT_OFFICER, itemInfo -> itemInfo.export().officer());
        addColumn(IMPORT_OFFICER, itemInfo -> itemInfo.$import().officer());
        addColumn(DELIVERY_COURIER, itemInfo -> itemInfo.delivery().courier());
    }

}
