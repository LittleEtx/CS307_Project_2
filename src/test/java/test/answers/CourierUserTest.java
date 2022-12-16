package test.answers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class CourierUserTest implements Serializable {
    public HashMap<List<Object>, Boolean> newItem = new HashMap<>();

    public HashMap<List<Object>, Boolean> setItemState = new HashMap<>();
}
