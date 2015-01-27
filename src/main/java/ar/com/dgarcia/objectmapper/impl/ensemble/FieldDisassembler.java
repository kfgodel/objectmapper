package ar.com.dgarcia.objectmapper.impl.ensemble;

import ar.com.dgarcia.objectmapper.api.MapperException;
import ar.com.dgarcia.objectmapper.api.ensemble.ObjectDisassembler;
import ar.com.dgarcia.objectmapper.api.ensemble.cache.Cache;
import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyInstruction;
import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyPlan;
import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;
import ar.com.dgarcia.objectmapper.impl.ensemble.cache.WeakMapCache;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.DisassemblyArrayPlan;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.instructions.FieldGetterInstruction;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.instructions.GetAndTransformInstruction;
import ar.com.dgarcia.objectmapper.impl.ensemble.transformers.NoChange;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.fields.TypeField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This type represents the disassembler that takes the fields in the object to create the mapping
 * Created by kfgodel on 20/11/14.
 */
public class FieldDisassembler implements ObjectDisassembler {

    private DisassemblyTransformer valueTransformer;
    private Cache<Class<?>, DisassemblyPlan> planPerType;


    @Override
    public Map<String, Object> disassemble(Object instance) {
        if(instance == null){
            return null;
        }
        DisassemblyPlan plan = getPlanFor(instance.getClass());
        DisassemblyInstruction[] instructions = plan.getInstructions();

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < instructions.length; i++) {
            DisassemblyInstruction instruction = instructions[i];
            map.put(instruction.getPartName(), instruction.getPartFrom(instance));
        }
        return map;
    }

    @Override
    public DisassemblyTransformer getDisassemblyTransformer() {
        if(valueTransformer == null){
            throw new MapperException("A value transformer was not defined for this field disassembler");
        }
        return valueTransformer;
    }

    @Override
    public void setDisassemblyTransformer(DisassemblyTransformer transformer) {
        this.valueTransformer = transformer;
    }

    /**
     * Gets or creates the disassembly plan for the given object type
     * @param instanceType The type of object to disassemble
     * @return The plan to use for objects of the given type
     */
    private DisassemblyPlan getPlanFor(Class<?> instanceType) {
        return planPerType.getOrCreateFor(instanceType, ()-> createPlanFor(instanceType));
    }

    /**
     * Creates a plan for the given object type
     * @param instanceType The type to define disassembly
     * @return The preferred plan to use
     */
    private DisassemblyPlan createPlanFor(Class<?> instanceType) {
        List<DisassemblyInstruction> disassemblyInstructions = Diamond.of(instanceType).fields().all()
                .filter(TypeField::isInstanceMember)
                .map(this::createInstructionFor)
                .collect(Collectors.toList());
        return DisassemblyArrayPlan.create(disassemblyInstructions);
    }

    private DisassemblyInstruction createInstructionFor(TypeField instanceField) {
        Class<?> valueType = instanceField.type().nativeTypes().get();
        Function<Object, Object> disassemblyTransformer = getDisassemblyTransformer().getTransformerFor(valueType);

        FieldGetterInstruction getterInstruction = FieldGetterInstruction.create(instanceField);
        if(disassemblyTransformer == NoChange.INSTANCE){
            // This is an optimization to avoid conversion if not needed
            return getterInstruction;
        }
        return GetAndTransformInstruction.create(getterInstruction, disassemblyTransformer);
    }

    public static FieldDisassembler create() {
        FieldDisassembler disassembler = new FieldDisassembler();
        disassembler.planPerType = WeakMapCache.create();
        return disassembler;
    }

    public static FieldDisassembler create(DisassemblyTransformer valueTransformer) {
        FieldDisassembler disassembler = create();
        disassembler.setDisassemblyTransformer(valueTransformer);
        valueTransformer.setObjectDisassembler(disassembler);
        return disassembler;
    }


}
