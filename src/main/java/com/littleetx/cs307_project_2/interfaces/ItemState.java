package com.littleetx.cs307_project_2.interfaces;

/**
 * <p>
 * Corresponding to column ItemState given in record.csv
 * <p>
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
