package ar.com.dgarcia.objectmapper.impl.converters;

import ar.com.dgarcia.objectmapper.api.TypeMapper;

import java.util.function.Function;

/**
 * This type represents the converter of arrays to lists
 * Created by kfgodel on 20/11/14.
 */
public class ObjectToMapConverter implements Function<Object, Object> {

    private TypeMapper mapper;

    @Override
    public Object apply(Object value) {
        return mapper.toMap(value);
    }

    public static ObjectToMapConverter create(TypeMapper mapper) {
        ObjectToMapConverter arrayToListConverter = new ObjectToMapConverter();
        arrayToListConverter.mapper = mapper;
        return arrayToListConverter;
    }

}
