package com.littleetx.cs307_project_2.database.database_type;

import java.io.Serializable;

public class TaxInfo implements Serializable {
    public record Key(
            int cityId,
            String item_type
    ) {
    }

    public static class Value {
        public Double export_rate;
        public Double import_rate;

        public Value(Double export_rate, Double import_rate) {
            this.export_rate = export_rate;
            this.import_rate = import_rate;
        }
    }
}
