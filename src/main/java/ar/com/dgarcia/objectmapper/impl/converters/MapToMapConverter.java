package ar.com.dgarcia.objectmapper.impl.converters;

import ar.com.dgarcia.objectmapper.api.MapSafeTypeConverter;

import java.util.*;
import java.util.function.Function;

/**
 * This type represents the converter of arrays to lists
 * Created by kfgodel on 20/11/14.
 */
public class MapToMapConverter implements Function<Object, Object> {

    private MapSafeTypeConverter converter;

    @Override
    public Object apply(Object value) {
        Map<String, Object> asMap = (Map) value;


        Map<String, Object> translatedMap = new LinkedHashMap<>();
        Set<Map.Entry<String, Object>> sourceEntries = asMap.entrySet();
        for (Map.Entry<String, Object> sourceEntry : sourceEntries) {
            translatedMap.put(sourceEntry.getKey(), converter.convertForMap(sourceEntry.getValue()));
        }
        return translatedMap;
    }

    public static MapToMapConverter create(MapSafeTypeConverter converter) {
        MapToMapConverter arrayToListConverter = new MapToMapConverter();
        arrayToListConverter.converter = converter;
        return arrayToListConverter;
    }

}
