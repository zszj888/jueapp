package com.somecom.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecom.entity.Role;
import org.springframework.util.ReflectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dto2JosnBody {
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(convert(Role.class));
    }

    /**
     * not all ,but typical
     */
    private static final Map<String, String> defaultValue = Stream.of(new String[][]{
            {"byte", "1"},
            {"int", "11"},
            {"float", "2.99"},
            {"double", "999.9999"},
            {"boolean", "false"},
            {"long", "10000"},
            {"java.lang.String", "string"},
            {"java.lang.Integer", "0"},
            {"java.lang.Byte", "0"},
            {"java.lang.Boolean", "false"},
            {"java.lang.Long", "1000"},
            {"java.lang.Float", "1.99"},
            {"java.lang.Double", "9.9999"},
            {"java.lang.Date", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())},
            {"java.lang.Collection", "[]"},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static String convert(Class<?> clz) {
        Map<String, String> jsonBody = new HashMap<>();
        ReflectionUtils.doWithLocalFields(clz, field -> {
            field.setAccessible(true);
            if (!"serialVersionUID".equals(field.getName()) && defaultValue.containsKey(field.getType().getName())) {
                jsonBody.put(field.getName(), defaultValue.get(field.getType().getName()));
            }
        });
        try {
            return new ObjectMapper().writeValueAsString(jsonBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
