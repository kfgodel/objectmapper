package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;
import ar.com.kfgodel.diamond.api.fields.TypeField;

/**
 * This type represents a direct property mapping, that needs no value conversion
 * Created by kfgodel on 19/11/14.
 */
public class DirectEntryDescription implements PropertyEntryDescription {

    private String propertyName;
    private TypeField field;


    @Override
    public String getName() {
        return propertyName;
    }

    @Override
    public Object getValueFrom(Object instance) {
        return field.getValueFrom(instance);
    }

    public static DirectEntryDescription create(TypeField field) {
        DirectEntryDescription description = new DirectEntryDescription();
        description.field = field;
        description.propertyName = field.name();
        return description;
    }

}
