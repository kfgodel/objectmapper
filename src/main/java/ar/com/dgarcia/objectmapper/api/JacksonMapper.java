package ar.com.dgarcia.objectmapper.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by kfgodel on 18/11/14.
 */
public class JacksonMapper implements TypeMapper {

    private ObjectMapper internalMapper;

    @Override
    public Map<String, Object> toMap(Object instance) {
        return internalMapper.convertValue(instance, Map.class);
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        return null;
    }

    public static JacksonMapper create() {
        JacksonMapper mapper = new JacksonMapper();
        mapper.internalMapper = new ObjectMapper();
        return mapper;
    }

}
