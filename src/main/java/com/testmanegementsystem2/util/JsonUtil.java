package com.testmanegementsystem2.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T fromJson(HttpServletRequest req, Class<T> clazz) throws IOException {
        return MAPPER.readValue(req.getInputStream(), clazz);
    }

    public static void toJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.setContentType("application/json");
        MAPPER.writeValue(resp.getOutputStream(), obj);
    }
}
