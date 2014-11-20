package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.instructions;

import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyInstruction;
import ar.com.kfgodel.diamond.api.fields.TypeField;

import java.lang.reflect.Field;

/**
 * This type represents a disassembly instruction that gets the value from a field
 * Created by kfgodel on 20/11/14.
 */
public class FieldGetterInstruction implements DisassemblyInstruction {

    private String fieldName;
    private Field field;

    public static FieldGetterInstruction create(TypeField field) {
        FieldGetterInstruction description = new FieldGetterInstruction();
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
    public Object getPartFrom(Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unexpected access restriction", e);
        }
    }
}
