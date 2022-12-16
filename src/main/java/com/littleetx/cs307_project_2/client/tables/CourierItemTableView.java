package com.littleetx.cs307_project_2.client.tables;

public class CourierItemTableView extends ItemTableView {
    public static final String HANDLE_TYPE = "Handle Type";

    /**
     * @param courierName null for not showing Handle_type column
     */
    public CourierItemTableView(String courierName) {
        super();
        addItemBasicInfo();
        if (courierName != null) {
            addColumn(HANDLE_TYPE, itemInfo -> {
                if (courierName.equals(itemInfo.retrieval().courier())) {
                    return "Retrieval";
                } else if (courierName.equals(itemInfo.delivery().courier())) {
                    return "Delivery";
                } else {
                    return "Unknown";
                }
            });
        }
        addRouteInfo();
    }
}
