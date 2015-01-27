package ar.com.kfgodel.objectmapper.tests.mapper;

import ar.com.kfgodel.objectmapper.tests.testObjects.ReferencedObject;
import ar.com.kfgodel.objectmapper.tests.testObjects.TypicalObject;

import java.util.*;
import java.util.function.Function;

/**
 * This type represents a custom made typicalObject disassembler
 * Created by kfgodel on 25/11/14.
 */
public class CustomMadeTypicalObjectDisassembler implements Function<TypicalObject, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(TypicalObject object) {
        if(object == null){
            return null;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(TypicalObject.intPrimitive_FIELD, object.getIntPrimitive());
        map.put(TypicalObject.stringPrimitive_FIELD, object.getStringPrimitive());
        map.put(TypicalObject.arrayPrimitive_FIELD, convertToList(object.getArrayPrimitive()));
        map.put(TypicalObject.otherObject_FIELD, apply(object.getOtherObject()));
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
            maps.add(apply(otherObject));
        }
        return maps;
    }

    private List<String> convertToList(String[] arrayPrimitive) {
        if(arrayPrimitive == null){
            return null;
        }
        return Arrays.asList(arrayPrimitive);
    }


    public static CustomMadeTypicalObjectDisassembler create() {
        CustomMadeTypicalObjectDisassembler disassembler = new CustomMadeTypicalObjectDisassembler();
        return disassembler;
    }

}
