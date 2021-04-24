package com.waes.diff.data;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Component;

@Component
public class CacheClient {

    public static final String DIFF = "diff";

    private final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

    public String put(final String id, final String element) {
        IMap<String, String> map = hazelcastInstance.getMap(DIFF);
        return map.putIfAbsent(id, element);
    }

    public String get(final String id) {
        IMap<String, String> map = hazelcastInstance.getMap(DIFF);
        return map.get(id);
    }
}
