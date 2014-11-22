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
        map.put(TypicalObject.referencedMap_FIELD, translateToMap(object.getReferencedMap()));
        return map;
    }

    private Map<String, Object> translateToMap(Map<String, ReferencedObject> referencedMap) {
        Map<String, Object> translated = new LinkedHashMap<>();
        Set<Map.Entry<String, ReferencedObject>> entries = referencedMap.entrySet();
        for (Map.Entry<String, ReferencedObject> entry : entries) {
            translated.put(entry.getKey(), convertToMap(entry.getValue()));
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
        if(map == null){
            return null;
        }
        TypicalObject object = new TypicalObject();
        object.setIntPrimitive((Integer) map.get(TypicalObject.intPrimitive_FIELD));
        object.setStringPrimitive((String) map.get(TypicalObject.stringPrimitive_FIELD));
        object.setArrayPrimitive(convertFromList((List<String>) map.get(TypicalObject.arrayPrimitive_FIELD)));
        object.setOtherObject(fromMap((Map<String, Object>) map.get(TypicalObject.otherObject_FIELD), TypicalObject.class));
        object.setOtherObjects(convertFromListOfMaps((List<Map<String, Object>>) map.get(TypicalObject.otherObjects_FIELD)));
        object.setAnotherReference(convertFromMap((Map<String, Object>) map.get(TypicalObject.anotherReference_FIELD)));
        object.setReferencedMap(translateFromMap((Map<String, Object>) map.get(TypicalObject.referencedMap_FIELD)));
        return (T) object;

//        Map<String, Object> map = new LinkedHashMap<>();
//        map.put(TypicalObject.intPrimitive_FIELD, object.getIntPrimitive());
//        map.put(TypicalObject.stringPrimitive_FIELD, object.getStringPrimitive());
//        map.put(TypicalObject.arrayPrimitive_FIELD, convertToList(object.getArrayPrimitive()));
//        map.put(TypicalObject.otherObject_FIELD, toMap(object.getOtherObject()));
//        map.put(TypicalObject.otherObjects_FIELD, convertToListOfMaps(object.getOtherObjects()));
//        map.put(TypicalObject.anotherReference_FIELD, convertToMap(object.getAnotherReference()));
//        map.put(TypicalObject.referencedMap_FIELD, translateMap(object.getReferencedMap()));
//        return map;
    }

    private Map<String, ReferencedObject> translateFromMap(Map<String, Object> referencedMap) {
        Map<String, ReferencedObject> translated = new LinkedHashMap<>();
        Set<Map.Entry<String, Object>> entries = referencedMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            translated.put(entry.getKey(), convertFromMap((Map<String, Object>) entry.getValue()));
        }
        return translated;
    }

    private ReferencedObject convertFromMap(Map<String, Object> map) {
        if(map== null){
            return null;
        }
        Integer age = (Integer) map.get(ReferencedObject.age_FIELD);
        String name = (String) map.get(ReferencedObject.name_FIELD);
        return ReferencedObject.create(age, name);
    }

    private List<TypicalObject> convertFromListOfMaps(List<Map<String, Object>> maps) {
        List<TypicalObject> objects = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            objects.add(fromMap(map, TypicalObject.class));
        }
        return objects;
    }

    private String[] convertFromList(List<String> strings) {
        return strings.toArray(new String[strings.size()]);
    }

    public static TypicalObjectMapper create() {
        TypicalObjectMapper mapper = new TypicalObjectMapper();
        return mapper;
    }

}
