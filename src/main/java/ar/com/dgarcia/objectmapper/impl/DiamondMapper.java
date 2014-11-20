package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;
import ar.com.dgarcia.objectmapper.api.MapSafeTypeConverter;
import ar.com.dgarcia.objectmapper.api.TypeMapDescription;
import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.dgarcia.objectmapper.impl.converters.*;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.fields.TypeField;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This type represents a mapper implemented with diamond reflection
 * Created by kfgodel on 18/11/14.
 */
public class DiamondMapper implements TypeMapper {

    private Map<Class<?>, TypeMapDescription> descriptionPerType = new WeakHashMap<>();
    private MapSafeTypeConverter converter;

    @Override
    public Map<String, Object> toMap(Object instance) {
        if(instance == null){
            return null;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        TypeMapDescription mapDescription = getDescriptionFor(instance.getClass());
        PropertyEntryDescription[] properties = mapDescription.getProperties();
        for (int i = 0; i < properties.length; i++) {
            PropertyEntryDescription property = properties[i];
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
        Function<Object, Object> specificConverter = converter.getSpecificConverterFor(instanceClass);
        if(specificConverter == NoConverter.INSTANCE){
            // This is an optimization to avoid conversion if not needed
            return directDescription;
        }
        return ConvertedEntryDescription.create(directDescription, specificConverter);
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        return null;
    }

    public static DiamondMapper create() {
        DiamondMapper mapper = new DiamondMapper();
        mapper.converter = MapSafeTypeConverterImpl.create(mapper);
        return mapper;
    }

}
