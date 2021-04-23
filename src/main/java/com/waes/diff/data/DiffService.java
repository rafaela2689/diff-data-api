package com.waes.diff.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@Service
public class DiffService {

    private HazelcastInstance hazelcastInstance;

    private Gson gson;

    private ConcurrentMap<String, String> retrieveMap() {
        return hazelcastInstance.getMap("map");
    }

    @Autowired
    public DiffService(final HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.gson = new Gson();
    }

    public void save(final String id, JsonObject element, String side) {
        final JsonObject elementSide = new JsonObject();
        elementSide.add(side, element);
        String fullObject = retrieveMap().get(id);

        JsonObject jsonConverted = this.gson.fromJson(fullObject, JsonObject.class);
        if (jsonConverted == null) {
            jsonConverted = new JsonObject();
        }
        jsonConverted.add(side, element);
        String serializeObj = this.gson.toJson(jsonConverted);
        retrieveMap().put(id, serializeObj);
    }

    public Map<String, String> getDiffElement(final String id) {
        Map<String, String> result = new HashMap<>();
        if (id.isBlank()) {
            result.put("result", "Id is not valid");
            return result;
        }

        final String stringObj = retrieveMap().get(id);
        JsonObject response = this.gson.fromJson(stringObj, JsonObject.class);

        final JsonElement right = response.get("right");
        Map<String, Object> rightMap = gson.fromJson(right, Map.class);
        final JsonElement left = response.get("left");
        Map<String, Object> leftObj = gson.fromJson(left, Map.class);

        if (Objects.isNull(rightMap)) {
            result.put("result", "Empty right side");
            return result;
        }

        if (Objects.isNull(leftObj)) {
            result.put("result", "Empty left side");
            return result;
        }

        if (rightMap.equals(leftObj)) {
            result.put("result", "Equals side");
            return result;
        }

        if (rightMap.size() != leftObj.size()) {
            result.put("result", "Different size!");
            return result;
        }

        final String differences = Maps.difference(rightMap, leftObj).toString();
        result.put("result", differences);
        return result;
    }
}
