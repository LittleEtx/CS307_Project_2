package main.interfaces;

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
