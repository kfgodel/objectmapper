package ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers;

import ar.com.dgarcia.objectmapper.api.ensemble.ObjectAssembler;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.Map;
import java.util.function.Function;

/**
 * This type represents teh functional adapter of teh object assembler
 * Created by kfgodel on 21/11/14.
 */
public class ObjectAssemblerAdapter implements Function<Object, Object> {

    private ObjectAssembler assembler;
    private TypeInstance expectedType;

    @Override
    public Object apply(Object value) {
        Map<String, Object> asMap = (Map<String, Object>) value;
        return assembler.assemble(asMap, expectedType);
    }

    public static ObjectAssemblerAdapter create(ObjectAssembler assembler, TypeInstance expectedType) {
        ObjectAssemblerAdapter assemblerAdapter = new ObjectAssemblerAdapter();
        assemblerAdapter.assembler = assembler;
        assemblerAdapter.expectedType = expectedType;
        return assemblerAdapter;
    }

}
