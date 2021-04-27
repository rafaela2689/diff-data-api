package com.waes.diffdata.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.waes.diffdata.service.DiffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("v1/diff")
public class DiffController {

    private DiffService diffService;
    private Gson gson;

    @Autowired
    public DiffController(final DiffService diffService) {
        this.diffService = diffService;
        this.gson = new Gson();
    }

    @PostMapping("/{id}/right")
    public String saveDiffRight(@PathVariable final String id,
                              @RequestBody final String rightData) {
        if (rightData == null || rightData.isEmpty()) {
            return "Invalid data informed!";
        }
        final JsonObject jsonData = getJsonObject(rightData);
        return this.diffService.save(id, jsonData, "right");
    }

    @PostMapping("{id}/left")
    public String saveDiffLeft(@PathVariable final String id,
                              @RequestBody final String left) {
        if (left == null || left.isEmpty()) {
            return "Invalid data informed!";
        }
        final JsonObject jsonData = getJsonObject(left);
        return this.diffService.save(id, jsonData, "left");
    }

    @GetMapping("{id}")
    public Map<String, String> getDiff(@PathVariable final String id) {
        return this.diffService.getDiffElement(id);
    }

    private JsonObject getJsonObject(@RequestBody String rightData) {
        byte[] decoded = Base64.getDecoder().decode(rightData);
        final String decodedData = new String(decoded);
        return gson.fromJson(decodedData, JsonObject.class);
    }
}
