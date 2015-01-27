package ar.com.kfgodel.objectmapper.tests.mapper;

import ar.com.kfgodel.objectmapper.tests.testObjects.ReferencedObject;
import ar.com.kfgodel.objectmapper.tests.testObjects.TypicalObject;

import java.util.*;
import java.util.function.Function;

/**
 * This type creates an object from a map expecting it to be a TypicalObject
 * Created by kfgodel on 25/11/14.
 */
public class CustomMadeTypicalObjectAssembler implements Function<Map<String, Object>, TypicalObject> {

    private String[] convertFromList(List<String> strings) {
        return strings.toArray(new String[strings.size()]);
    }

    @Override
    public TypicalObject apply(Map<String, Object> map) {
        if(map == null){
            return null;
        }
        TypicalObject object = new TypicalObject();
        object.setIntPrimitive((Integer) map.get(TypicalObject.intPrimitive_FIELD));
        object.setStringPrimitive((String) map.get(TypicalObject.stringPrimitive_FIELD));
        object.setArrayPrimitive(convertFromList((List<String>) map.get(TypicalObject.arrayPrimitive_FIELD)));
        object.setOtherObject(apply((Map<String, Object>) map.get(TypicalObject.otherObject_FIELD)));
        object.setOtherObjects(convertFromListOfMaps((List<Map<String, Object>>) map.get(TypicalObject.otherObjects_FIELD)));
        object.setAnotherReference(convertFromMap((Map<String, Object>) map.get(TypicalObject.anotherReference_FIELD)));
        object.setReferencedMap(translateFromMap((Map<String, Object>) map.get(TypicalObject.referencedMap_FIELD)));
        return object;
    }

    private List<TypicalObject> convertFromListOfMaps(List<Map<String, Object>> maps) {
        List<TypicalObject> objects = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            objects.add(apply(map));
        }
        return objects;
    }

    private ReferencedObject convertFromMap(Map<String, Object> map) {
        if(map== null){
            return null;
        }
        Integer age = (Integer) map.get(ReferencedObject.age_FIELD);
        String name = (String) map.get(ReferencedObject.name_FIELD);
        return ReferencedObject.create(age, name);
    }

    private Map<String, ReferencedObject> translateFromMap(Map<String, Object> referencedMap) {
        Map<String, ReferencedObject> translated = new LinkedHashMap<>();
        Set<Map.Entry<String, Object>> entries = referencedMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            translated.put(entry.getKey(), convertFromMap((Map<String, Object>) entry.getValue()));
        }
        return translated;
    }

    public static CustomMadeTypicalObjectAssembler create() {
        CustomMadeTypicalObjectAssembler assembler = new CustomMadeTypicalObjectAssembler();
        return assembler;
    }

}
