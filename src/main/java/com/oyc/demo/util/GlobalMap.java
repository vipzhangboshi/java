package com.oyc.demo.util;

import java.util.HashMap;
import java.util.Map;

public class GlobalMap {
    private static GlobalMap instance;
    private Map<String, String> map;

    private GlobalMap() {
        map = new HashMap<>();
    }

    public static synchronized GlobalMap getInstance() {
        if (instance == null) {
            instance = new GlobalMap();
        }
        return instance;
    }

    public String getKey() {
        if (map.containsKey("Token")) {
            return map.get("Token");
        }
        return null;
    }

    public void add(String equipmentId, String ctx) {
        map.put(equipmentId, ctx);
    }
}

