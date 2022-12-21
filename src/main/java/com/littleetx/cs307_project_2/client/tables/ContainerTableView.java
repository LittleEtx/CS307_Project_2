package com.littleetx.cs307_project_2.client.tables;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import main.interfaces.ContainerInfo;

public class ContainerTableView extends TableViewBase<ContainerInfo> {
    public static final String CODE = "Code";
    public static final String TYPE = "Type";
    public static final String STATE = "State";

    public ContainerTableView() {
        super();

        addColumn(CODE, ContainerInfo::code);
        addColumn(TYPE, info -> DatabaseMapping.getContainerTypeVisualStr(info.type()));
        addColumn(STATE, info -> info.using() ? "Using" : "Idle");
    }
}
