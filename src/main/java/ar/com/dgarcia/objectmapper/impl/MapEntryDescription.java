package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This type represents the description of a array value description
 * Created by kfgodel on 20/11/14.
 */
public class MapEntryDescription implements PropertyEntryDescription {

    private DirectEntryDescription directDescription;
    private DiamondMapper mapper;

    @Override
    public String getName() {
        return directDescription.getName();
    }

    @Override
    public Object getValueFrom(Object instance) {
        Map<String, Object> asMap = (Map) directDescription.getValueFrom(instance);


        Map<String, Object> translatedMap = new LinkedHashMap<>();
        Set<Map.Entry<String, Object>> sourceEntries = asMap.entrySet();
        for (Map.Entry<String, Object> sourceEntry : sourceEntries) {
            translatedMap.put(sourceEntry.getKey(), mapper.recursiveConversionFrom(sourceEntry.getValue()));
        }
        return translatedMap;
    }

    public static MapEntryDescription create(DirectEntryDescription direct, DiamondMapper mapper) {
        MapEntryDescription description = new MapEntryDescription();
        description.directDescription = direct;
        description.mapper = mapper;
        return description;
    }

}
