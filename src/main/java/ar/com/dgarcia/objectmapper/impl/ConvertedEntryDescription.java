package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.PropertyEntryDescription;

import java.util.function.Function;

/**
 * This type represents the description of a array value description
 * Created by kfgodel on 20/11/14.
 */
public class ConvertedEntryDescription implements PropertyEntryDescription {

    private DirectEntryDescription directDescription;
    private Function<Object, Object> converter;

    @Override
    public String getName() {
        return directDescription.getName();
    }

    @Override
    public Object getValueFrom(Object instance) {
        Object originalValue = directDescription.getValueFrom(instance);
        Object convertedValue = converter.apply(originalValue);
        return convertedValue;
    }

    public static ConvertedEntryDescription create(DirectEntryDescription direct, Function<Object, Object> converter) {
        ConvertedEntryDescription description = new ConvertedEntryDescription();
        description.directDescription = direct;
        description.converter = converter;
        return description;
    }

}
