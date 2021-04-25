package com.waes.diff.data.integrationtests;

import com.google.gson.JsonObject;
import com.hazelcast.core.Hazelcast;
import com.waes.diff.data.Application;
import com.waes.diff.data.repository.DataRepository;
import com.waes.diff.data.service.DiffService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
public class DiffServiceTest {

    @Autowired
    private DiffService diffService;

    @Autowired
    private DataRepository cacheClient;

    @Test
    public void shouldSaveElementSideToRepository() {
        final String idMap = "1a-2b-3c";

        final JsonObject element = new JsonObject();
        element.addProperty("name", "Mary");
        element.addProperty("job", "singer");

        final String side = "right";

        diffService.save(idMap, element, side);

        final String objectSaved = cacheClient.get(idMap);
        assertFalse(objectSaved.isEmpty());
        assertTrue(objectSaved.contains("Mary"));
    }

    @AfterEach
    public void cleanup() throws Exception {
        Hazelcast.shutdownAll();
    }

}
