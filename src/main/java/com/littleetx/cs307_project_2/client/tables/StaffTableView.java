package com.littleetx.cs307_project_2.client.tables;

import javafx.beans.property.StringProperty;
import main.interfaces.StaffInfo;

import java.util.Map;

import static com.littleetx.cs307_project_2.database.DatabaseMapping.getStaffAuthorityVisualStr;

public class StaffTableView extends TableViewBase<Map.Entry<Integer, StaffInfo>> {
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String AUTHORITY = "Authority";
    public static final String COMPANY = "Company";
    public static final String CITY = "City";
    public static final String GENDER = "Gender";
    public static final String AGE = "Age";
    public static final String PHONE = "Phone";

    public StaffTableView() {
        super();
        addColumn(ID, entry -> entry.getKey().toString());
        addColumn(NAME, info -> info.getValue().basicInfo().name());
        addColumn(AUTHORITY, info ->
                getStaffAuthorityVisualStr(info.getValue().basicInfo().type()));
        addColumn(COMPANY, info -> info.getValue().company());
        addColumn(CITY, info -> info.getValue().city());
        addColumn(GENDER, info -> info.getValue().isFemale() ? "Female" : "Male");
        addColumn(AGE, info -> String.valueOf(info.getValue().age()));
        addColumn(PHONE, info -> info.getValue().phoneNumber());
    }

    @Override
    public void setFilter(StringProperty filterText) {
        setFilterText(filterText, info ->
                filter(info.getKey().toString(), filterText.getValue())
                        || filter(info.getValue().basicInfo().name(), filterText.getValue())
                        || filter(getStaffAuthorityVisualStr(info.getValue().basicInfo().type()), filterText.getValue())
                        || filter(info.getValue().company(), filterText.getValue())
                        || filter(info.getValue().city(), filterText.getValue())
                        || filter(info.getValue().isFemale() ? "Female" : "Male", filterText.getValue())
                        || filter(String.valueOf(info.getValue().age()), filterText.getValue())
                        || filter(info.getValue().phoneNumber(), filterText.getValue())
        );
    }
}
