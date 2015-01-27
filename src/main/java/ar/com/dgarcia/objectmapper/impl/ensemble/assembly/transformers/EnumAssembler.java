package ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers;

import ar.com.dgarcia.objectmapper.api.MapperException;

import java.util.function.Function;

/**
 * This type represents the enum assembler to re-generate enum values from strings
 * Created by ikari on 27/01/2015.
 */
public class EnumAssembler implements Function<Object, Object> {

    private Class<? extends Enum> expectedType;

    @Override
    public Object apply(Object o) {
        String enumName = (String) o;
        Enum enumValue = null;
        try {
            enumValue = Enum.valueOf(expectedType, enumName);
        } catch (IllegalArgumentException e) {
            throw new MapperException("An enum value["+enumName+"] could not be found on type["+expectedType+"]",e);
        }
        return enumValue;
    }

    public static EnumAssembler create(Class<? extends Enum> expectedType) {
        EnumAssembler assembler = new EnumAssembler();
        assembler.expectedType = expectedType;
        return assembler;
    }

}
