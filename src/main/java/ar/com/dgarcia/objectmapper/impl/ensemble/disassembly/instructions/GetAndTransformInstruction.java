package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.instructions;

import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyInstruction;

import java.util.function.Function;

/**
 * This type represents a disassembly instruction that transforms the value obtained from a field
 * Created by kfgodel on 20/11/14.
 */
public class GetAndTransformInstruction implements DisassemblyInstruction {

    private DisassemblyInstruction getterInstruction;
    private Function<Object, Object> transformer;

    @Override
    public String getPartName() {
        return getterInstruction.getPartName();
    }

    @Override
    public Object getPartFrom(Object instance) {
        Object originalValue = getterInstruction.getPartFrom(instance);
        Object convertedValue = transformer.apply(originalValue);
        return convertedValue;
    }

    public static GetAndTransformInstruction create(DisassemblyInstruction getterInstruction, Function<Object, Object> transformer) {
        GetAndTransformInstruction instruction = new GetAndTransformInstruction();
        instruction.getterInstruction = getterInstruction;
        instruction.transformer = transformer;
        return instruction;
    }

}
