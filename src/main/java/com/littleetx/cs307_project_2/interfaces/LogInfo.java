package com.littleetx.cs307_project_2.interfaces;

import java.io.Serializable;

/**
 * <p>
 * Information of the given user, including his/her name, staff type and password
 * <p>
*/

public record LogInfo(
        String name,
        StaffType type,
        String password
) implements Serializable {
    public enum StaffType {
        SustcManager,
        CompanyManager,
        Courier,
        SeaportOfficer
    }
}
