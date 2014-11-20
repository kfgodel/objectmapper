package ar.com.dgarcia.objectmapper.impl.converters;

import ar.com.dgarcia.objectmapper.api.MapSafeTypeConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

/**
 * This type represents the converter of arrays to lists
 * Created by kfgodel on 20/11/14.
 */
public class CollectionToListConverter implements Function<Object, Object> {

    private MapSafeTypeConverter converter;

    @Override
    public Object apply(Object value) {
        Collection<Object> asCollection = (Collection) value;
        ArrayList<Object> objects = new ArrayList<>(asCollection.size());
        for (Object element : asCollection) {
            objects.add(converter.convertForMap(element));
        }
        return objects;
    }

    public static CollectionToListConverter create(MapSafeTypeConverter converter) {
        CollectionToListConverter arrayToListConverter = new CollectionToListConverter();
        arrayToListConverter.converter = converter;
        return arrayToListConverter;
    }

}
