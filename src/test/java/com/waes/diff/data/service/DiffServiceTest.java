package com.waes.diff.data.service;

import com.google.gson.JsonObject;
import com.waes.diff.data.CacheClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DiffServiceTest {

    @InjectMocks
    private DiffService diffService;

    @Mock
    private CacheClient cacheClient;

    @Test
    public void shouldSaveDiffRight() {
        final String idMap = "1a-2b-3c";
        final JsonObject element = new JsonObject();
        element.addProperty("name", "Mary");
        element.addProperty("job", "singer");

        final String side = "right";

        when(cacheClient.get(idMap)).thenReturn(null);
        when(cacheClient.put(eq(idMap), any())).thenReturn("{\"name\":\"Mary\",\"job\":\"singer\"}");
        diffService.save(idMap, element, side);

        verify(cacheClient, times(1)).put(eq(idMap), any());
    }

    @Test
    public void shouldSaveDiffLeft() {
        final String idMap = "1a-2b-3c";
        final JsonObject element = new JsonObject();
        element.addProperty("name", "Mary");
        element.addProperty("job", "teacher");

        final String side = "left";

        when(cacheClient.get(idMap)).thenReturn(null);
        when(cacheClient.put(eq(idMap), any())).thenReturn("{\"name\":\"Mary\",\"job\":\"teacher\"}");
        diffService.save(idMap, element, side);

        verify(cacheClient, times(1)).put(eq(idMap), any());
    }

    @Test
    public void shouldReturnDiffWhenIsSameSize() {
        final String idMap = "1a-2b-3c";

        when(cacheClient.get(idMap)).thenReturn("{\"right\":{\"name\":\"Mary\",\"job\":\"singer\"},\"left\":{\"name\":\"Mary\",\"job\":\"singer\"}}");

        final Map<String, String> result = diffService.getDiffElement(idMap);

        verify(cacheClient, times(1)).get(eq(idMap));

        assertEquals(result.get("result"), "Equals side");
    }

    @Test
    public void shouldReturnDiffWhenSizesAreDifferent() {
        final String idMap = "1a-2b-3c";

        when(cacheClient.get(idMap)).thenReturn("{\"right\":{\"name\":\"Mary\",\"job\":\"singer\"},\"left\":{\"name\":\"Mary\"}}");

        final Map<String, String> result = diffService.getDiffElement(idMap);

        verify(cacheClient, times(1)).get(eq(idMap));

        assertEquals(result.get("result"), "Different size!");
    }

    @Test
    public void shouldReturnWhenRightSideIsEmpty() {
        final String idMap = "1a-2b-3c";

        when(cacheClient.get(idMap)).thenReturn("{\"left\":{\"name\":\"Mary\",\"job\":\"singer\"}}");

        final Map<String, String> result = diffService.getDiffElement(idMap);

        verify(cacheClient, times(1)).get(eq(idMap));

        assertEquals(result.get("result"), "Empty right side");
    }

    @Test
    public void shouldReturnWhenLeftIsEmpty() {
        final String idMap = "1a-2b-3c";

        when(cacheClient.get(idMap)).thenReturn("{\"right\":{\"name\":\"Mary\",\"job\":\"singer\"}}");

        final Map<String, String> result = diffService.getDiffElement(idMap);

        verify(cacheClient, times(1)).get(eq(idMap));

        assertEquals(result.get("result"), "Empty left side");
    }

    @Test
    public void shouldShowDifferencesWhenSidesAreEqual() {
        final String idMap = "1a-2b-3c";

        when(cacheClient.get(idMap)).thenReturn("{\"right\":{\"name\":\"Mary\",\"job\":\"singer\"},\"left\":{\"name\":\"Mary\",\"job\":\"journalist\"}}");

        final Map<String, String> result = diffService.getDiffElement(idMap);

        verify(cacheClient, times(1)).get(eq(idMap));

        assertEquals(result.get("result"), "not equal: value differences={job=(singer, journalist)}");
    }
}
