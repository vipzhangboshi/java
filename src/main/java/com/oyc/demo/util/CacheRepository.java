package com.oyc.demo.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CacheRepository {
    private final Map<String, String> tokenCache = new HashMap<>();

    public int delEq(String equipmentId, String ctxId) {
        if (tokenCache().containsKey(equipmentId)) {
            tokenCache().remove(equipmentId);
        }
        return tokenCache().size();
    }

    private Map<String, String> tokenCache() {
        return tokenCache;
    }

    public void add(String equipmentId, String ctx) {
        tokenCache.put(equipmentId, ctx);
    }

    public String getChannelId(String equipmentId) {
        return tokenCache().get(equipmentId);
    }


}
