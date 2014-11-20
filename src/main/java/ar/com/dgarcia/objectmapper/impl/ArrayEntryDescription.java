package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;

import java.util.ArrayList;

/**
 * This type represents the description of a array value description
 * Created by kfgodel on 20/11/14.
 */
public class ArrayEntryDescription implements PropertyEntryDescription {

    private DirectEntryDescription directDescription;
    private DiamondMapper mapper;

    @Override
    public String getName() {
        return directDescription.getName();
    }

    @Override
    public Object getValueFrom(Object instance) {
        Object[] asArray = (Object[]) directDescription.getValueFrom(instance);
        ArrayList<Object> objects = new ArrayList<>(asArray.length);
        for (Object element : asArray) {
            objects.add(mapper.recursiveConversionFrom(element));
        }
        return objects;
    }

    public static ArrayEntryDescription create(DirectEntryDescription direct, DiamondMapper mapper) {
        ArrayEntryDescription description = new ArrayEntryDescription();
        description.directDescription = direct;
        description.mapper = mapper;
        return description;
    }

}
