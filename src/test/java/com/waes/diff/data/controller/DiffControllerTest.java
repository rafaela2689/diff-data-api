package com.waes.diff.data.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.diff.data.service.DiffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DiffController.class)
public class DiffControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DiffService diffService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCallSaveDiffServiceWhenAccessEndpointSaveDiffRight() throws Exception {
        final String id = "1a-2b-3c";
        final String side = "right";
        final String encodedJsonBase64 = "eyAKICAgICJuYW1lIjogIlJhZmFlbGEgQ2F2YWxjYW50ZSBkZSBBcmHDumpvIiwKICAgICJhZ2UiOiAzMSwgCiAgICAiam9iIjogIlNvZnR3YXJlIERldmVsb3BlciIKfQ==";

        final String responseString = mvc.perform(
                post("/v1/diff/" + id + "/" + side)
                        .content(encodedJsonBase64)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(diffService, times(1)).save(eq(id), any(), eq(side));
    }

    @Test
    public void shouldCallSaveDiffServiceWhenAccessEndpointSaveDiffLeft() throws Exception {
        final String id = "1a-2b-3c";
        final String side = "left";
        final String encodedJsonBase64 = "eyAKICAgICJuYW1lIjogIlJhZmFlbGEgQ2F2YWxjYW50ZSBkZSBBcmHDumpvIiwKICAgICJhZ2UiOiAzMSwgCiAgICAiam9iIjogIlNvZnR3YXJlIERldmVsb3BlciIKfQ==";

        final String responseString = mvc.perform(
                post("/v1/diff/" + id + "/" + side)
                        .content(encodedJsonBase64)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(diffService, times(1)).save(eq(id), any(), eq(side));
    }

    @Test
    public void shouldCallGetDiffServiceWhenAccessEndpointGetDiff() throws Exception {
        final String id = "1a-2b-3c";

        final Map<String, String> result = new HashMap<>();
        result.put("result", "Equals size");

        when(diffService.getDiffElement(id)).thenReturn(result);

        final String responseString = mvc.perform(
                get("/v1/diff/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final Map<String, String> response = objectMapper.readValue(responseString, Map.class);
        verify(diffService, times(1)).getDiffElement(eq(id));

        assertEquals(result, response);
    }
}
