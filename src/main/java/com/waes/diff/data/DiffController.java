package com.waes.diff.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

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

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/{id}/right"
    )
    public void saveDiffRight(@PathVariable final String id,
                              @RequestBody final String right) {
        final String decodedData = String.valueOf(Base64.getDecoder().decode(right));
        final JsonObject jsonData = gson.fromJson(decodedData, JsonObject.class);
        this.diffService.save(id, jsonData, "right");
    }

    @PostMapping("{id}/left")
    public void saveDiffLeft(@PathVariable final String id,
                              @RequestBody final String left) {
        final String decodedData = String.valueOf(Base64.getDecoder().decode(left));
        final JsonObject jsonData = gson.fromJson(decodedData, JsonObject.class);
        this.diffService.save(id, jsonData, "left");
    }

    @GetMapping("{id}")
    public JsonElement getDiff(@PathVariable final String id) {
        final String diff = this.diffService.getDiffElement(id);

        return JsonParser.parseString(gson.toJson(diff));
    }
}
