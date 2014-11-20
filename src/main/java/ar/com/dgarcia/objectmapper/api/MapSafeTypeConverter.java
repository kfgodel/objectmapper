package ar.com.dgarcia.objectmapper.api;

import java.util.function.Function;

/**
 * This type represents a converter of values to move between objects and maps
 * Created by kfgodel on 20/11/14.
 */
public interface MapSafeTypeConverter {

    /**
     * Converts the given object to a map referentiable value
     * @param element The object to convert
     * @return The converted map-safe value
     */
    Object convertForMap(Object element);

    /**
     * Returns the converter to use for the given type
     */
    Function<Object, Object> getSpecificConverterFor(Class<?> elementClass);
}
