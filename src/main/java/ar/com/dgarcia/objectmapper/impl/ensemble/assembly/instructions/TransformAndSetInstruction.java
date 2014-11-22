package ar.com.dgarcia.objectmapper.impl.ensemble.assembly.instructions;

import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyInstruction;

import java.util.function.Function;

/**
 * This type represents the instruction that transforms the value to a valid type, and sets it in the instance
 * Created by kfgodel on 21/11/14.
 */
public class TransformAndSetInstruction implements AssemblyInstruction {

    private AssemblyInstruction setterInstruction;
    private Function<Object, Object> transformer;

    @Override
    public String getPartName() {
        return setterInstruction.getPartName();
    }

    @Override
    public void setPartOn(Object instance, Object part) {
        Object convertedValue = transformer.apply(part);
        setterInstruction.setPartOn(instance, convertedValue);
    }

    public static TransformAndSetInstruction create(AssemblyInstruction setterInstruction, Function<Object, Object> transformer) {
        TransformAndSetInstruction instruction = new TransformAndSetInstruction();
        instruction.setterInstruction = setterInstruction;
        instruction.transformer = transformer;
        return instruction;
    }

}
