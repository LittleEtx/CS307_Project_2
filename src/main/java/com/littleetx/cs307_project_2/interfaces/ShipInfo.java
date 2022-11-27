package com.littleetx.cs307_project_2.interfaces;

import java.io.Serializable;

/**
 * <p>
 * Full information of a given ship
 * <p>
 * @param owner: the owner company's name
 * @param sailing: whether this ship is sailing or not
*/
public record ShipInfo(
        String name,
        String owner,
        boolean sailing
) implements Serializable {
}
