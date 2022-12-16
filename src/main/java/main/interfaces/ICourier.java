package main.interfaces;

public interface ICourier {
    boolean newItem(LogInfo log, ItemInfo item);

    boolean setItemState(LogInfo log, String name, ItemState s);
}
