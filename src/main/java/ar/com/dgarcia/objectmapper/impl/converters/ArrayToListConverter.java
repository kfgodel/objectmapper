package ar.com.dgarcia.objectmapper.impl.converters;

import ar.com.dgarcia.objectmapper.api.MapSafeTypeConverter;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * This type represents the converter of arrays to lists
 * Created by kfgodel on 20/11/14.
 */
public class ArrayToListConverter implements Function<Object, Object> {

    private MapSafeTypeConverter converter;

    @Override
    public Object apply(Object value) {
        Object[] asArray = (Object[]) value;
        ArrayList<Object> objects = new ArrayList<>(asArray.length);
        for (Object element : asArray) {
            objects.add(converter.convertForMap(element));
        }
        return objects;
    }

    public static ArrayToListConverter create(MapSafeTypeConverter converter) {
        ArrayToListConverter arrayToListConverter = new ArrayToListConverter();
        arrayToListConverter.converter = converter;
        return arrayToListConverter;
    }

}
