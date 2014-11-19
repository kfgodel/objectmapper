package ar.com.dgarcia.objectmapper.api;

import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.fields.TypeField;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This type represents a mapper implemented with diamond reflection
 * Created by kfgodel on 18/11/14.
 */
public class DiamondMapper implements TypeMapper {

    private Map<Class<?>, Collection<TypeField>> fieldsPerType = new HashMap<>();

    @Override
    public Map<String, Object> toMap(Object instance) {
        if(instance == null){
            return null;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        getFieldsFor(instance.getClass())
                .forEach((field) -> map.put(field.name(), recursiveConversionFrom(field.getValueFrom(instance))));
        return map;
    }

    private Stream<TypeField> getFieldsFor(Class<?> aClass){
        Collection<TypeField> typeFields = fieldsPerType.get(aClass);
        if(typeFields == null){
            TypeInstance instanceType = Diamond.of(aClass);
            typeFields = instanceType.fields().all()
                    .filter(TypeField::isInstanceMember)
                    .collect(Collectors.toList());
            fieldsPerType.put(aClass, typeFields);
        }
        return typeFields.stream();
    }

    private Object recursiveConversionFrom(Object instance) {
        if(instance == null){
            return null;
        }
        Class<?> instanceClass = instance.getClass();
        if(instanceClass.isPrimitive()){
            return instance;
        }
        if(Number.class.isAssignableFrom(instanceClass)){
            return instance;
        }
        if(CharSequence.class.isAssignableFrom(instanceClass)){
            return instance;
        }
        if(instanceClass.getComponentType() != null){
            return arrayAsList((Object[])instance);
        }
        if(Collection.class.isAssignableFrom(instanceClass)){
            return collectionAsList((Collection) instance);
        }
        if(Map.class.isAssignableFrom(instanceClass)){
            return translateMap((Map) instance);
        }
        return toMap(instance);
    }

    private Object translateMap(Map<String, Object> map) {
        Map<String, Object> translatedMap = new LinkedHashMap<>();
        Set<Map.Entry<String, Object>> sourceEntries = map.entrySet();
        for (Map.Entry<String, Object> sourceEntry : sourceEntries) {
            translatedMap.put(sourceEntry.getKey(), recursiveConversionFrom(sourceEntry.getValue()));
        }
        return translatedMap;
    }

    private Object collectionAsList(Collection collection) {
        ArrayList<Object> objects = new ArrayList<>();
        for (Object element : collection) {
            objects.add(recursiveConversionFrom(element));
        }
        return objects;
    }

    private Object arrayAsList(Object[] array) {
        ArrayList<Object> objects = new ArrayList<>(array.length);
        for (Object element : array) {
            objects.add(recursiveConversionFrom(element));
        }
        return objects;
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        return null;
    }

    public static DiamondMapper create() {
        DiamondMapper diamondMapper = new DiamondMapper();
        return diamondMapper;
    }

}
