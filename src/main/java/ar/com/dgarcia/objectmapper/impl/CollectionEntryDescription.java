package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This type represents the description of a array value description
 * Created by kfgodel on 20/11/14.
 */
public class CollectionEntryDescription implements PropertyEntryDescription {

    private DirectEntryDescription directDescription;
    private DiamondMapper mapper;

    @Override
    public String getName() {
        return directDescription.getName();
    }

    @Override
    public Object getValueFrom(Object instance) {
        Collection<Object> collection = (Collection<Object>) directDescription.getValueFrom(instance);
        ArrayList<Object> objects = new ArrayList<>(collection.size());
        for (Object element : collection) {
            objects.add(mapper.recursiveConversionFrom(element));
        }
        return objects;
    }

    public static CollectionEntryDescription create(DirectEntryDescription direct, DiamondMapper mapper) {
        CollectionEntryDescription description = new CollectionEntryDescription();
        description.directDescription = direct;
        description.mapper = mapper;
        return description;
    }

}
