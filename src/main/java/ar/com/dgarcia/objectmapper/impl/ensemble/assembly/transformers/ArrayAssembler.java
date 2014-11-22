package ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers;

import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.List;
import java.util.function.Function;

/**
 * This type represents the array assembler
 * Created by kfgodel on 21/11/14.
 */
public class ArrayAssembler implements Function<Object,Object> {

    private AssemblyTransformer valueTransformer;
    private Function<Integer, Object[]> arraySupplier;
    private TypeInstance arrayComponentType;

    @Override
    public Object apply(Object value) {
        List<Object> asList = (List<Object>) value;

        Object[] assembledContent = arraySupplier.apply(asList.size());
        for (int i = 0; i < asList.size(); i++) {
            Object original = asList.get(i);
            Object assembled = valueTransformer.transform(original, arrayComponentType);
            assembledContent[i] = assembled;
        }
        return assembledContent;
    }

    public static ArrayAssembler create(AssemblyTransformer genericTransformer, TypeInstance arrayType) {
        ArrayAssembler arrayAssembler = new ArrayAssembler();
        arrayAssembler.valueTransformer = genericTransformer;
        arrayAssembler.arraySupplier = (Function)arrayType.constructors().withNativeParameters(int.class).get();
        arrayAssembler.arrayComponentType = arrayType.componentType().get();
        return arrayAssembler;
    }
}
