package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;
import ar.com.dgarcia.objectmapper.api.TypeMapDescription;

import java.util.List;

/**
 * This type represents the implementation of a map description
 * Created by kfgodel on 19/11/14.
 */
public class TypeMapDescriptionImpl implements TypeMapDescription {

    private List<PropertyEntryDescription> propertyList;

    public static TypeMapDescriptionImpl create(List<PropertyEntryDescription> propertyList) {
        TypeMapDescriptionImpl description = new TypeMapDescriptionImpl();
        description.propertyList = propertyList;
        return description;
    }

    @Override
    public List<PropertyEntryDescription> getProperties() {
        return propertyList;
    }
}
