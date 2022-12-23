package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import javafx.beans.property.StringProperty;
import main.interfaces.ItemInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class ItemTableView extends TableViewBase<ItemInfo> {
    public static final String ITEM_NAME = "Name";
    public static final String ITEM_TYPE = "Type";
    public static final String PRICE = "Price";
    public static final String ITEM_STATE = "State";
    public static final String RETRIEVAL_CITY = "Retrieval City";
    public static final String RETRIEVAL_COURIER = "Retrieval Courier";
    public static final String EXPORT_CITY = "Export City";
    public static final String EXPORT_OFFICER = "Export Officer";
    public static final String IMPORT_CITY = "Import City";
    public static final String IMPORT_OFFICER = "Import Officer";
    public static final String DELIVERY_CITY = "Delivery City";
    public static final String DELIVERY_COURIER = "Delivery Courier";
    public static String IMPORT_TAX = "Import Tax";
    public static String EXPORT_TAX = "Export Tax";


    public ItemTableView() {
        super();
    }

    @Override
    public void setFilter(StringProperty filterText) {
        setFilterText(filterText,
                info -> {
                    for (var pre : filterList) {
                        if (pre.test(info, filterText.getValue())) {
                            return true;
                        }
                    }
                    return false;
                }
        );
    }

    private final List<BiPredicate<ItemInfo, String>> filterList = new ArrayList<>();

    public void addItemBasicInfo() {
        addColumn(ITEM_NAME, ItemInfo::name);
        addColumn(ITEM_TYPE, ItemInfo::$class);
        addColumn(PRICE, itemInfo -> convertPrice((int) itemInfo.price()), true);
        addColumn(ITEM_STATE, itemInfo -> DatabaseMapping.getStateVisualString(itemInfo.state()));

        filterList.add((info, key) ->
                filter(info.name(), key)
                        || filter(info.$class(), key)
                        || filter(DatabaseMapping.getStateVisualString(info.state()), key));
    }

    public void addRouteInfo() {
        addColumn(RETRIEVAL_CITY, itemInfo -> itemInfo.retrieval().city());
        addColumn(EXPORT_CITY, itemInfo -> itemInfo.export().city());
        addColumn(IMPORT_CITY, itemInfo -> itemInfo.$import().city());
        addColumn(DELIVERY_CITY, itemInfo -> itemInfo.delivery().city());

        filterList.add((info, key) ->
                filter(info.retrieval().city(), key)
                        || filter(info.export().city(), key)
                        || filter(info.$import().city(), key)
                        || filter(info.delivery().city(), key));
    }

    public void addStaffInfo() {
        addColumn(RETRIEVAL_COURIER, itemInfo -> itemInfo.retrieval().courier());
        addColumn(EXPORT_OFFICER, itemInfo -> itemInfo.export().officer());
        addColumn(IMPORT_OFFICER, itemInfo -> itemInfo.$import().officer());
        addColumn(DELIVERY_COURIER, itemInfo -> itemInfo.delivery().courier());

        filterList.add((info, key) ->
                filter(info.retrieval().courier(), key)
                        || filter(info.export().officer(), key)
                        || filter(info.$import().officer(), key)
                        || filter(info.delivery().courier(), key));
    }

}
