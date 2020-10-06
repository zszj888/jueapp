package com.somecom.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public class JSON {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode read(String jsonStr) {
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonStr);
        } catch (IOException ignored) {
        }
        if (Objects.isNull(jsonNode)) {
            throw new RuntimeException("Read JsonNode from \"" + jsonStr + "\" ,come out NULL");
        }
        return jsonNode;
    }
}
