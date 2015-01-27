package ar.com.dgarcia.objectmapper.impl.ensemble;

import ar.com.dgarcia.objectmapper.api.MapperException;
import ar.com.dgarcia.objectmapper.api.ensemble.ObjectAssembler;
import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyInstruction;
import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyPlan;
import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.dgarcia.objectmapper.api.ensemble.cache.Cache;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.AssemblyArrayPlan;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.instructions.FieldSetterInstruction;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.instructions.TransformAndSetInstruction;
import ar.com.dgarcia.objectmapper.impl.ensemble.cache.WeakMapCache;
import ar.com.dgarcia.objectmapper.impl.ensemble.transformers.NoChange;
import ar.com.kfgodel.diamond.api.fields.TypeField;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This type represents an object assembler that takes the map keys as field names
 *
 * Created by kfgodel on 21/11/14.
 */
public class FieldAssembler implements ObjectAssembler {

    private Cache<TypeInstance, AssemblyPlan> planPerType;
    private AssemblyTransformer valueTransformer;

    @Override
    public <T> T assemble(Map<String, Object> map, TypeInstance expectedType) {
        if(map == null){
            return null;
        }
        AssemblyPlan plan = getPlanFor(expectedType);
        AssemblyInstruction[] instructions = plan.getInstructions();

        T createdInstance = plan.<T>getInstanceSupplier().get();
        for (int i = 0; i < instructions.length; i++) {
            AssemblyInstruction instruction = instructions[i];
            Object part = map.get(instruction.getPartName());
            instruction.setPartOn(createdInstance, part);
        }
        return createdInstance;
    }

    private <T> AssemblyPlan getPlanFor(TypeInstance expectedType) {
        return planPerType.getOrCreateFor(expectedType, ()-> createPlanFor(expectedType));
    }

    private AssemblyPlan createPlanFor(TypeInstance expectedType) {
        List<AssemblyInstruction> disassemblyInstructions = expectedType.fields().all()
                .filter(TypeField::isInstanceMember)
                .map(this::createInstructionFor)
                .collect(Collectors.toList());
        return AssemblyArrayPlan.create(disassemblyInstructions, expectedType.constructors().niladic().get());
    }

    private AssemblyInstruction createInstructionFor(TypeField instanceField) {
        TypeInstance fieldType = instanceField.type();
        Function<Object, Object> assemblyTransformer = getValueTransformer().getTransformerFor(fieldType);

        FieldSetterInstruction setterInstruction = FieldSetterInstruction.create(instanceField);
        if(assemblyTransformer == NoChange.INSTANCE){
            // This is an optimization to avoid conversion if not needed
            return setterInstruction;
        }
        return TransformAndSetInstruction.create(setterInstruction, assemblyTransformer);
    }

    public static FieldAssembler create(AssemblyTransformer assemblyTransformer) {
        FieldAssembler assembler = new FieldAssembler();
        assembler.planPerType = WeakMapCache.create();
        assembler.valueTransformer = assemblyTransformer;
        return assembler;
    }

    public AssemblyTransformer getValueTransformer() {
        if(valueTransformer == null){
            throw new MapperException("A value transformer was not defined for this field assembler");
        }
        return valueTransformer;
    }
}
