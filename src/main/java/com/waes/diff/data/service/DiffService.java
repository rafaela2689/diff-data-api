package com.waes.diff.data.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.waes.diff.data.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.waes.diff.data.utils.Messages.*;

@Service
public class DiffService {

    private Gson gson;

    private DataRepository cacheClient;

    @Autowired
    public DiffService(final DataRepository cacheClient) {
        this.cacheClient = cacheClient;
        this.gson = new Gson();
    }

    /**
     * Save the object of each diff side
     * @param id key to reference the object
     * @param element content to be diff-ed
     * @param side represents the side of the diff, it can be right or left
     */
    public void save(final String id, JsonObject element, String side) {
        // get object by id
        String compareStr = this.cacheClient.get(id);

        // convert it to json
        JsonObject compareJson = this.gson.fromJson(compareStr, JsonObject.class);

        // create a new object if it null
        if (compareJson == null) {
            compareJson = new JsonObject();
        }
        // add element side
        compareJson.add(side, element);
        String compareJsonStringify = this.gson.toJson(compareJson); // convert object to string

        // add object in the cache
        this.cacheClient.put(id, compareJsonStringify);
    }

    /**
     * Get the diff given a key
     * @param id references the object id
     * @return returns a map with the result of the diff
     * { "result": "Equals size" }
     * { "result": "Different size" }
     * { "result": "Differences" }
     */
    public Map<String, String> getDiffElement(final String id) {
        Map<String, String> result = new HashMap<>();
        // make sure the id is valid
        if (id.isBlank()) {
            result.put("result", INVALID_ID);
            return result;
        }

        // getting the object by id
        final String compareStr = this.cacheClient.get(id);
        JsonObject compareObj = this.gson.fromJson(compareStr, JsonObject.class);

        final JsonElement right = compareObj.get("right");
        Map<String, Object> rightMap = gson.fromJson(right, Map.class);
        final JsonElement left = compareObj.get("left");
        Map<String, Object> leftMap = gson.fromJson(left, Map.class);

        // checking if there is something to compare
        if (Objects.isNull(rightMap)) {
            result.put("result", RIGHT_EMPTY_SIDE);
            return result;
        }

        if (Objects.isNull(leftMap)) {
            result.put("result", LEFT_EMPTY_SIDE);
            return result;
        }

        // checking if objects are equal
        if (rightMap.equals(leftMap)) {
            result.put("result", EQUAL_OBJECTS);
            return result;
        }

        // checking if objects have different size
        if (rightMap.size() != leftMap.size()) {
            result.put("result", DIFFERENT_SIZE);
            return result;
        }

        // checking the objects differences, since they are equal
        final String differences = Maps.difference(rightMap, leftMap).toString();
        result.put("result", DIFFERENCES_FOUND + differences);
        return result;
    }
}
