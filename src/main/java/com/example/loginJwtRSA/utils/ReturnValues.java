package com.example.loginJwtRSA.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnValues {
    public static Map<String, Object> createReturnListCount(List<?> list) {
        Map map = new HashMap<String, Object>();
        map.put("data", list);
        map.put("count", list.size());
        return map;
    }

    public static Map<String, Object> createReturnCount(int count) {
        Map map = new HashMap<String, Object>();
        map.put("count", count);
        return map;
    }

    public static Map<String, Object> createReturnMessage(String message) {
        Map map = new HashMap<String, Object>();
        map.put("message", message);
        return map;
    }
}
