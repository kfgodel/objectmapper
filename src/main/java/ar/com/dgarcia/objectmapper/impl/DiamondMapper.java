package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;
import ar.com.dgarcia.objectmapper.api.TypeMapDescription;
import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.fields.TypeField;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This type represents a mapper implemented with diamond reflection
 * Created by kfgodel on 18/11/14.
 */
public class DiamondMapper implements TypeMapper {

    private Map<Class<?>, TypeMapDescription> descriptionPerType = new WeakHashMap<>();

    @Override
    public Map<String, Object> toMap(Object instance) {
        if(instance == null){
            return null;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        TypeMapDescription mapDescription = getDescriptionFor(instance.getClass());
        List<PropertyEntryDescription> properties = mapDescription.getProperties();
        for (PropertyEntryDescription property : properties) {
            map.put(property.getName(), property.getValueFrom(instance));
        }
        return map;
    }

    private TypeMapDescription getDescriptionFor(Class<?> aClass) {
        TypeMapDescription existingDescription = descriptionPerType.get(aClass);
        if(existingDescription != null){
            return existingDescription;
        }
        TypeMapDescription createdDescription = createDescriptionFor(aClass);
        descriptionPerType.put(aClass, createdDescription);
        return createdDescription;
    }

    private TypeMapDescription createDescriptionFor(Class<?> aClass) {
        TypeInstance instanceType = Diamond.of(aClass);
        List<PropertyEntryDescription> propertyList = instanceType.fields().all()
                .filter(TypeField::isInstanceMember)
                .map(this::createDescriptionFor)
                .collect(Collectors.toList());
        return TypeMapDescriptionImpl.create(propertyList);
    }

    private PropertyEntryDescription createDescriptionFor(TypeField instanceField) {
        TypeInstance fieldType = instanceField.type();
        DirectEntryDescription directDescription = DirectEntryDescription.create(instanceField);
        Class<?> instanceClass = fieldType.rawClasses().get();
        if(instanceClass.isPrimitive()){
            return directDescription;
        }
        if(Number.class.isAssignableFrom(instanceClass)){
            return directDescription;
        }
        if(CharSequence.class.isAssignableFrom(instanceClass)){
            return directDescription;
        }
        if(instanceClass.getComponentType() != null){
            return ArrayEntryDescription.create(directDescription, this);
        }
        if(Collection.class.isAssignableFrom(instanceClass)){
            return CollectionEntryDescription.create(directDescription, this);
        }
        if(Map.class.isAssignableFrom(instanceClass)){
            return MapEntryDescription.create(directDescription, this);
        }
        return ObjectEntryDescription.create(directDescription, this);
    }

    public Object recursiveConversionFrom(Object instance) {
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
