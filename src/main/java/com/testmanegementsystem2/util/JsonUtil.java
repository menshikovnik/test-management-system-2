package com.testmanegementsystem2.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonUtil {
    private static final Pattern KV =
            Pattern.compile("\"([^\"]+)\":\"?([^\"]*)\"?");

    public static Map<String,String> read(HttpServletRequest req)
            throws IOException {
        String txt = new String(req.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8);
        Matcher m = KV.matcher(txt);
        Map<String,String> r = new HashMap<>();
        while (m.find()) r.put(m.group(1), m.group(2));
        return r;
    }

    public static void write(HttpServletResponse resp, Object obj)
            throws IOException {
        String json = toJson(obj);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(json);
    }

    private static String toJson(Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof String) {
            return "\"" + escape((String) o) + "\"";
        }
        if (o instanceof Number || o instanceof Boolean) {
            return o.toString();
        }
        if (o instanceof Map) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Object e : ((Map<?,?>) o).entrySet()) {
                Map.Entry<?,?> en = (Map.Entry<?,?>) e;
                if (!first) sb.append(",");
                sb.append(toJson(en.getKey().toString()))
                        .append(":")
                        .append(toJson(en.getValue()));
                first = false;
            }
            return sb.append("}").toString();
        }
        if (o instanceof Iterable) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : (Iterable<?>) o) {
                if (!first) sb.append(",");
                sb.append(toJson(item));
                first = false;
            }
            return sb.append("]").toString();
        }
        if (o.getClass().isArray()) {
            int len = Array.getLength(o);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < len; i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(Array.get(o, i)));
            }
            return sb.append("]").toString();
        }

        StringBuilder sb = new StringBuilder("{");
        Field[] fields = o.getClass().getDeclaredFields();
        boolean first = true;
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object value = f.get(o);
                if (!first) sb.append(",");
                sb.append("\"").append(f.getName()).append("\":")
                        .append(toJson(value));
                first = false;
            } catch (IllegalAccessException ignored) {}
        }
        return sb.append("}").toString();
    }

    private static String escape(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int)c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}

