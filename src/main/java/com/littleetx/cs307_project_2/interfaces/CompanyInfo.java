package com.littleetx.cs307_project_2.interfaces;

import java.io.Serializable;

/**
 * <p>
 * Full information of a company
 * <p>
 *
 * parameters of the constructor:
 * @param name: name of the company
 * @param shipNames: names of all ships belong to this company
 * @param managerNames: names of all company managers managing this company's records
 */
public record CompanyInfo(
        String name,
        String[] shipNames,
        String[] managerNames
) implements Serializable {
}
