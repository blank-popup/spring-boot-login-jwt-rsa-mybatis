package com.example.loginJwtRSA.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReturnValues {
    public static Map<String, Object> createReturnListCount(List<?> list) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("data", list);
        map.put("count", list.size());
        return map;
    }

    public static Map<String, Object> createReturnCount(int count) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("count", count);
        return map;
    }

    public static Map<String, Object> createReturnMessage(String message) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("message", message);
        return map;
    }
}
