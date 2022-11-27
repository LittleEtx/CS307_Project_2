package cs307.project2.interfaces;

/*
 * <p>
 * Corresponding to column ItemState given in record.csv
 * <p>
 * @classname: ItemState
*/
public enum ItemState {
	PickingUp,
	ToExportTransporting,
	ExportChecking,
	ExportCheckFailed,
	PackingToContainer,
	WaitingForShipping,
	Shipping,
	UnpackingFromContainer,
	ImportChecking,
	ImportCheckFailed,
	FromImportTransporting,
	Delivering,
	Finish
}
