package ru.peredera.mock.rest.repository.model;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;


public class JsonConverter {
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.getFactory().enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    @SneakyThrows
    public static String convertToString(Response mjo) {
        return mapper.writeValueAsString(mjo);
    }

    @SneakyThrows
    public static <T> T convertToEntity(String dbData, Class<T> aClass) {
        return mapper.readValue(dbData, aClass);
    }
}
