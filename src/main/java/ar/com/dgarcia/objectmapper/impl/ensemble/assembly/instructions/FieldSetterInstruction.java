package ar.com.dgarcia.objectmapper.impl.ensemble.assembly.instructions;

import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyInstruction;
import ar.com.kfgodel.diamond.api.fields.TypeField;

import java.lang.reflect.Field;

/**
 * This type represents the isntruction that sets the value into the field of the object
 * Created by kfgodel on 21/11/14.
 */
public class FieldSetterInstruction implements AssemblyInstruction {

    private String fieldName;
    private Field field;

    public static FieldSetterInstruction create(TypeField field) {
        FieldSetterInstruction description = new FieldSetterInstruction();
        description.field = field.nativeType().get();
        description.field.setAccessible(true);
        description.fieldName = field.name();
        return description;
    }


    @Override
    public String getPartName() {
        return fieldName;
    }

    @Override
    public void setPartOn(Object instance, Object part) {
        try {
            field.set(instance, part);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unexpected access restriction", e);
        }
    }

}
