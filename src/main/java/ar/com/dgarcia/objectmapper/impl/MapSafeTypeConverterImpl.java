package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.MapSafeTypeConverter;
import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.dgarcia.objectmapper.impl.converters.*;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * This type represents the converter for values used in maps
 * Created by kfgodel on 20/11/14.
 */
public class MapSafeTypeConverterImpl implements MapSafeTypeConverter {


    private Map<Class<?>, Function<Object, Object>> converterPerType = new WeakHashMap<>();

    private ArrayToListConverter arrayToListConverter;
    private CollectionToListConverter collectionToListConverter;
    private MapToMapConverter mapToMapConverter;
    private ObjectToMapConverter objectToMapConverter;

    public static MapSafeTypeConverterImpl create(TypeMapper mapper) {
        MapSafeTypeConverterImpl converter = new MapSafeTypeConverterImpl();
        converter.arrayToListConverter = ArrayToListConverter.create(converter);
        converter.collectionToListConverter = CollectionToListConverter.create(converter);
        converter.mapToMapConverter = MapToMapConverter.create(converter);
        converter.objectToMapConverter = ObjectToMapConverter.create(mapper);
        return converter;
    }

    @Override
    public Object convertForMap(Object element) {
        if(element == null){
            return null;
        }
        Class<?> elementClass = element.getClass();
        Function<Object, Object> specificConverter = getSpecificConverterFor(elementClass);
        Object converted = specificConverter.apply(element);
        return converted;
    }

    public Function<Object, Object> getSpecificConverterFor(Class<?> elementClass) {
        Function<Object, Object> specificConverter = converterPerType.get(elementClass);
        if(specificConverter == null){
            specificConverter = defineConverterFor(elementClass);
            converterPerType.put(elementClass, specificConverter);
        }
        return specificConverter;
    }

    private Function<Object, Object> defineConverterFor(Class<?> instanceClass) {
        if(instanceClass.isPrimitive()){
            return NoConverter.INSTANCE;
        }
        if(Number.class.isAssignableFrom(instanceClass)){
            return NoConverter.INSTANCE;
        }
        if(CharSequence.class.isAssignableFrom(instanceClass)){
            return NoConverter.INSTANCE;
        }
        if(instanceClass.getComponentType() != null){
            return arrayToListConverter;
        }
        if(Collection.class.isAssignableFrom(instanceClass)){
            return collectionToListConverter;
        }
        if(Map.class.isAssignableFrom(instanceClass)){
            return mapToMapConverter;
        }
        return objectToMapConverter;
    }
}
