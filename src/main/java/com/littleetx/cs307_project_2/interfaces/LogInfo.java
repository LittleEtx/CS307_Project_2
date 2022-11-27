package cs307.project2.interfaces;

import java.io.Serializable;

/*
 * <p>
 * Information of the given user, including his/her name, staff type and password
 * <p>
 * @classname: LogInfo
*/

public record LogInfo(String name, StaffType type, String password) implements Serializable {
    public enum StaffType {
        SustcManager,
        CompanyManager,
        Courier,
        SeaportOfficer
    }
}
