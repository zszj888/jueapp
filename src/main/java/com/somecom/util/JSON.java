package com.somecom.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSON {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode read(String jsonStr) {
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonStr);
        } catch (IOException ignored) {
        }
        return jsonNode;
    }
}
