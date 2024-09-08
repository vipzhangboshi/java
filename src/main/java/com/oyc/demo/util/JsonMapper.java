package com.oyc.demo.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class JsonMapper {

    private static JsonMapper jsonMapper;
    private final ObjectMapper mapper;

    public JsonMapper() {
        this((Include) null);
    }

    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(Include.NON_NULL);
    }

    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return this.mapper.getTypeFactory().constructParametrizedType(parametrized, parametrized, parameterClasses);
    }


    public JsonMapper(Include include) {
        this.mapper = new ObjectMapper();
        if (include != null) {
            this.mapper.setSerializationInclusion(include);
        }

        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        this.mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public static JsonMapper alwaysMapper() {
        return new JsonMapper(Include.ALWAYS);
    }

    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    public static JsonMapper nonNullMapper() {
        return new JsonMapper(Include.NON_NULL);
    }

    public static JsonMapper nonDefaultMapper() {
        return new JsonMapper(Include.NON_DEFAULT);
    }

    public static JsonMapper getInstance() {
        if (null == jsonMapper) {
            jsonMapper = (new JsonMapper(Include.NON_DEFAULT)).enableSimple();
        }

        return jsonMapper;
    }

    public String toJson(Object object) {
        try {
            String json = this.mapper.writeValueAsString(object);
            return json != null ? json.replaceAll("\"null\"", "\"\"").replaceAll("null", "\"\"") : json;
        } catch (IOException var3) {
            return null;
        }
    }

    public String toNewJson(Object object) {
        try {
            String json = this.mapper.writeValueAsString(object);
            return json;
        } catch (IOException var3) {
            return null;
        }
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (org.springframework.util.StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                jsonString = this.escapeStr(jsonString);
                return this.mapper.readValue(jsonString, clazz);
            } catch (IOException var4) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        JsonMapper j = getInstance();
        List<Integer> i = new ArrayList<>();
        i.add(1231);
        i.add(45678);
        String json = j.toJson(i);
        List<Integer> result = (List) j.fromJson(json, List.class);

        for (Integer s : result) {
            System.out.println(s);
        }
    }

    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                jsonString = this.escapeStr(jsonString);
                return this.mapper.readValue(jsonString, javaType);
            } catch (IOException var4) {
                return null;
            }
        }
    }

    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public <T> T update(String jsonString, T object) {
        try {
            return this.mapper.readerForUpdating(object).readValue(jsonString);
        } catch (IOException ignored) {
        }
        return null;
    }

    public String toJsonP(String functionName, Object object) {
        return this.toJson(new JSONPObject(functionName, object));
    }

    public JsonMapper enableEnumUseToString() {
        this.mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    public JsonMapper enableSimple() {
        this.mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        this.mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return this;
    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    private String escapeStr(String json) {
        if (json != null && json.length() >= 1) {
            StringBuffer sb = new StringBuffer();
            int no = 0;

            for (int i = 0; i < json.length(); ++i) {
                char c = json.charAt(i);
                switch (c) {
                    case '\b':
                        if (no % 2 == 1) {
                            sb.append("\\b");
                        } else {
                            sb.append(c);
                        }
                        break;
                    case '\t':
                        if (no % 2 == 1) {
                            sb.append("\\t");
                        } else {
                            sb.append(c);
                        }
                        break;
                    case '\n':
                        if (no % 2 == 1) {
                            sb.append("\\n");
                        } else {
                            sb.append(c);
                        }
                    case '\r':
                        if (no % 2 == 1) {
                            sb.append("\\r");
                        } else {
                            sb.append(c);
                        }
                        break;
                    case '\f':
                        if (no % 2 == 1) {
                            sb.append("\\f");
                        } else {
                            sb.append(c);
                        }
                        break;
                    case '"':
                        ++no;
                        sb.append(c);
                        break;
                    default:
                        sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return json;
        }
    }
}
