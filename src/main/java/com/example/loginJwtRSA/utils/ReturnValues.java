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

    public static Map<String, Object> createReturnCountMessage(int count, String message) {
        Map map = new HashMap<String, Object>();
        map.put("count", count);
        map.put("message", message);
        return map;
    }

    public static Map<String, Object> createReturnStatusMessage(String status, String message) {
        Map map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("message", message);
        return map;
    }

    public static Map<String, Object> createReturnStatusMessageList(String status, String message, List<?> list) {
        Map map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", list);
        map.put("count", list.size());
        return map;
    }

    public static Map<String, Object> createReturnStatusMessageData(String status, String message, Object object) {
        Map map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", object);
        return map;
    }
}
