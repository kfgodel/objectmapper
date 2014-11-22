package ar.com.dgarcia.objectmapper.impl.ensemble.assembly;

import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyInstruction;
import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyPlan;

import java.util.List;
import java.util.function.Supplier;

/**
 * This type represents an assembly plan composed of an array of instructions
 * Created by kfgodel on 20/11/14.
 */
public class AssemblyArrayPlan implements AssemblyPlan {

    private AssemblyInstruction[] instructions;
    private Supplier<?> instanceSupplier;

    public static AssemblyArrayPlan create(List<AssemblyInstruction> instructionList, Supplier<?> instanceSupplier) {
        AssemblyArrayPlan description = new AssemblyArrayPlan();
        description.instructions = instructionList.toArray(new AssemblyInstruction[instructionList.size()]);
        description.instanceSupplier = instanceSupplier;
        return description;
    }

    @Override
    public AssemblyInstruction[] getInstructions() {
        return instructions;
    }

    @Override
    public <T> Supplier<T> getInstanceSupplier() {
        return (Supplier<T>) instanceSupplier;
    }
}
