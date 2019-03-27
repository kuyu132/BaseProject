package com.kuyuzhiqi.utils.common;

import java.util.Collection;

public class CollectionUtils {

    /**
     * 判断两个集合是否相等
     */
    public static boolean deepEquals(Collection collection1, Collection collection2) {
        if (collection1 == collection2) {
            return true;
        }
        if (collection1 == null || collection2 == null || collection1.size() != collection2.size()) {
            return false;
        }
        return collection1.containsAll(collection2);
    }
}
