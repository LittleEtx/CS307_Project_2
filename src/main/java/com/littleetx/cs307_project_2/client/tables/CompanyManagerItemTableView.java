package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.database.database_type.ItemFullInfo;
import javafx.beans.property.StringProperty;

import static com.littleetx.cs307_project_2.client.tables.ContainerTableView.*;
import static com.littleetx.cs307_project_2.client.tables.ItemTableView.*;
import static com.littleetx.cs307_project_2.client.tables.ShipTableView.SHIP_NAME;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.getContainerTypeVisualStr;
import static com.littleetx.cs307_project_2.database.DatabaseMapping.getStateVisualString;

public class CompanyManagerItemTableView extends TableViewBase<ItemFullInfo> {

    public CompanyManagerItemTableView() {
        super();
        addColumn(ITEM_NAME, info -> info.itemInfo().name());
        addColumn(ITEM_TYPE, info -> info.itemInfo().$class());
        addColumn(PRICE, info -> convertPrice((int) info.itemInfo().price()), true);
        addColumn(ITEM_STATE, info -> getStateVisualString(info.itemInfo().state()));

        addColumn(EXPORT_CITY, info -> info.itemInfo().export().city());
        addColumn(EXPORT_TAX, info -> convertPrice(info.itemInfo().export().tax()), true);
        addColumn(IMPORT_CITY, info -> info.itemInfo().$import().city());
        addColumn(IMPORT_TAX, info -> convertPrice(info.itemInfo().$import().tax()), true);

        addColumn(CODE, ItemFullInfo::containerCode);
        addColumn(CONTAINER_TYPE, info -> getContainerTypeVisualStr(info.containerType()));

        addColumn(SHIP_NAME, ItemFullInfo::shipName);

        addColumn(RETRIEVAL_CITY, info -> info.itemInfo().retrieval().city());
        addColumn(RETRIEVAL_COURIER, info -> info.itemInfo().retrieval().courier());
        addColumn(DELIVERY_CITY, info -> info.itemInfo().delivery().city());
        addColumn(DELIVERY_COURIER, info -> info.itemInfo().delivery().courier());
    }

    public enum ShowType {
        EXPORT, ON_SHIP, IMPORT
    }

    public void showColumn(ShowType type) {
        switch (type) {
            case EXPORT -> showColumns(ITEM_NAME, ITEM_TYPE, PRICE, EXPORT_CITY, EXPORT_TAX, CODE, CONTAINER_TYPE);
            case ON_SHIP ->
                    showColumns(ITEM_NAME, ITEM_TYPE, PRICE, STATE, CODE, CONTAINER_TYPE, SHIP_NAME, EXPORT_CITY, IMPORT_CITY);
            case IMPORT -> showColumns(ITEM_NAME, ITEM_TYPE, PRICE, IMPORT_CITY, IMPORT_TAX);
        }
    }

    @Override
    public void setFilter(StringProperty text) {
        setFilterText(text,
                info -> filter(info.itemInfo().name(), text.getValue())
                        || filter(info.itemInfo().$class(), text.getValue())
                        || filter(getStateVisualString(info.itemInfo().state()), text.getValue())
                        || filter(info.itemInfo().export().city(), text.getValue())
                        || filter(info.itemInfo().$import().city(), text.getValue())
                        || filter(info.itemInfo().retrieval().city(), text.getValue())
                        || filter(info.itemInfo().delivery().city(), text.getValue())
                        || filter(info.itemInfo().retrieval().courier(), text.getValue())
                        || filter(info.itemInfo().delivery().courier(), text.getValue())
                        || filter(info.containerCode(), text.getValue())
                        || filter(getContainerTypeVisualStr(info.containerType()), text.getValue())
                        || filter(info.shipName(), text.getValue())
        );
    }

}
