package com.littleetx.cs307_project_2.database_type;

public class TaxInfo{
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
