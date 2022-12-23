package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import javafx.beans.property.StringProperty;
import main.interfaces.ContainerInfo;

public class ContainerTableView extends TableViewBase<ContainerInfo> {
    public static final String CODE = "Code";
    public static final String CONTAINER_TYPE = "Type";
    public static final String STATE = "State";

    public ContainerTableView() {
        super();

        addColumn(CODE, ContainerInfo::code);
        addColumn(CONTAINER_TYPE, info -> DatabaseMapping.getContainerTypeVisualStr(info.type()));
        addColumn(STATE, info -> info.using() ? "Using" : "Idle");
    }

    @Override
    public void setFilter(StringProperty filterText) {
        setFilterText(filterText, info ->
                filter(info.code(), filterText.getValue())
                        || filter(DatabaseMapping.getContainerTypeVisualStr(info.type()), filterText.getValue())
                        || filter(info.using() ? "Using" : "Idle", filterText.getValue())
        );
    }
}
