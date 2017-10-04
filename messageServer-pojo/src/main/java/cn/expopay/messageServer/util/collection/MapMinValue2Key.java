package cn.expopay.messageServer.util.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class MapMinValue2Key {

    public static Integer getMinValue2Key(Map<Integer, Integer> map) {
        if (map == null) return null;
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        int minValue = Integer.valueOf(obj[0].toString());

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it.next();
            if (entry != null && entry.getValue() == minValue) {
                return(entry.getKey());
            }
        }
        return null;
    }

}
