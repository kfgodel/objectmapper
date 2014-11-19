package ar.com.kfgodel.objectmapper.tests.mapper;

import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.kfgodel.objectmapper.tests.testObjects.ReferencedObject;
import ar.com.kfgodel.objectmapper.tests.testObjects.TypicalObject;

import java.util.*;

/**
 * This type represents a custom implementation of the type mapper only for TypicalObject
 * Created by kfgodel on 18/11/14.
 */
public class TypicalObjectMapper implements TypeMapper {
    @Override
    public Map<String, Object> toMap(Object instance) {
        if(instance == null){
            return null;
        }
        TypicalObject object = (TypicalObject) instance;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(TypicalObject.intPrimitive_FIELD, object.getIntPrimitive());
        map.put(TypicalObject.stringPrimitive_FIELD, object.getStringPrimitive());
        map.put(TypicalObject.arrayPrimitive_FIELD, convertToList(object.getArrayPrimitive()));
        map.put(TypicalObject.otherObject_FIELD, toMap(object.getOtherObject()));
        map.put(TypicalObject.otherObjects_FIELD, convertToListOfMaps(object.getOtherObjects()));
        map.put(TypicalObject.anotherReference_FIELD, convertToMap(object.getAnotherReference()));
        map.put(TypicalObject.referencedMap_FIELD, translateMap(object.getReferencedMap()));
        return map;
    }

    private Map<String, Object> translateMap(Map<String, Object> referencedMap) {
        Map<String, Object> translated = new LinkedHashMap<>();
        Set<Map.Entry<String, Object>> entries = referencedMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            translated.put(entry.getKey(), convertToMap((ReferencedObject) entry.getValue()));
        }
        return translated;
    }

    private Map<String, Object> convertToMap(ReferencedObject reference) {
        if(reference == null){
            return null;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(ReferencedObject.age_FIELD, reference.getAge());
        map.put(ReferencedObject.name_FIELD, reference.getName());
        return map;
    }

    private List<Map<String, Object>> convertToListOfMaps(List<TypicalObject> otherObjects) {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (TypicalObject otherObject : otherObjects) {
            maps.add(toMap(otherObject));
        }
        return maps;
    }

    private List<String> convertToList(String[] arrayPrimitive) {
        if(arrayPrimitive == null){
            return null;
        }
        return Arrays.asList(arrayPrimitive);
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        return null;
    }

    public static TypicalObjectMapper create() {
        TypicalObjectMapper mapper = new TypicalObjectMapper();
        return mapper;
    }

}
