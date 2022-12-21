package com.littleetx.cs307_project_2.database.database_type;

import java.io.Serializable;

public record CityInfo(
        String name,
        boolean isSeaport
) implements Serializable {
}
