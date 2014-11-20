package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;

/**
 * This type represents the description of a array value description
 * Created by kfgodel on 20/11/14.
 */
public class ObjectEntryDescription implements PropertyEntryDescription {

    private DirectEntryDescription directDescription;
    private DiamondMapper mapper;

    @Override
    public String getName() {
        return directDescription.getName();
    }

    @Override
    public Object getValueFrom(Object instance) {
        Object value = directDescription.getValueFrom(instance);
        return mapper.toMap(value);
    }

    public static ObjectEntryDescription create(DirectEntryDescription direct, DiamondMapper mapper) {
        ObjectEntryDescription description = new ObjectEntryDescription();
        description.directDescription = direct;
        description.mapper = mapper;
        return description;
    }

}
