package com.littleetx.cs307_project_2.interfaces;

import java.io.Serializable;

/**
 * <p>
 * Full information of a given staff, its LogInfo is included here.
 * <p>
 */

public record StaffInfo(
        LogInfo basicInfo,
        String company,
        String city,
        boolean isFemale,
        int age,
        String phoneNumber
) implements Serializable {
}