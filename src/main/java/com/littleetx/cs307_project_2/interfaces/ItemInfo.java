package cs307.project2.interfaces;

import java.io.Serializable;

/*
/*
 * <p>
 * Full information of the item
 * <p>
 * @classname: ItemInfo
 */
public record ItemInfo(String name, String $class, double price, ItemState state, RetrievalDeliveryInfo retrieval, RetrievalDeliveryInfo delivery, ImportExportInfo $import, ImportExportInfo export) implements Serializable {
	public record ImportExportInfo(String city, String officer, double tax) implements Serializable {
	}
	public record RetrievalDeliveryInfo(String city, String courier) implements Serializable {
	}
}