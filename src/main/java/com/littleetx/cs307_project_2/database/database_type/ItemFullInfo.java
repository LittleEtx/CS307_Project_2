package com.littleetx.cs307_project_2.database.database_type;

import main.interfaces.ContainerInfo;
import main.interfaces.ItemInfo;

import java.io.Serializable;

public record ItemFullInfo(
        ItemInfo itemInfo,
        String shipName,
        String containerCode,
        ContainerInfo.Type containerType
) implements Serializable {
}
