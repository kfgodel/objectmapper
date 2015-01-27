package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.MapperException;
import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;
import ar.com.dgarcia.objectmapper.impl.ensemble.FieldAssembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.FieldDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.AssembledValueTransformer;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.DisassembledValueTransformer;
import ar.com.kfgodel.diamond.api.Diamond;

import java.util.Map;
import java.util.function.Function;

/**
 * This type represents a type mapper that uses assembly transformers to do assembly and disassembly
 * * 
 * Created by kfgodel on 26/01/15.
 */
public class TransformerMapper implements TypeMapper {
    
    private AssemblyTransformer assemblyTransformer;
    private DisassemblyTransformer disassemblyTransformer;
    
    @Override
    public Map<String, Object> toMap(Object instance) {
        if(instance == null){
            return null;
        }
        Class<?> sourceType = instance.getClass();
        Function<Object, Object> disassembler = disassemblyTransformer.getTransformerFor(sourceType);
        Object dissasembled = disassembler.apply(instance);
        Map<String, Object> asMap = null;
        try {
            asMap = (Map<String, Object>) dissasembled;
        } catch (Exception e) {
            throw new MapperException("Expected a Map disassembly of["+sourceType+"] but got["+dissasembled+"]. Using transformer ["+disassembler+"]");
        }
        return asMap;
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        if(map == null){
            return null;
        }
        Function<Object, Object> assembler = assemblyTransformer.getTransformerFor(Diamond.of(expectedType));
        Object assembled = assembler.apply(map);
        if(!expectedType.isInstance(assembled)){
            throw new MapperException("Expected an assembly of["+expectedType+"] but got["+assembled+"]. Using transformer["+assembler+"]");
        }
        return (T) assembled;
    }

    public AssemblyTransformer getAssemblyTransformer() {
        return assemblyTransformer;
    }

    public DisassemblyTransformer getDisassemblyTransformer() {
        return disassemblyTransformer;
    }

    public static TransformerMapper create() {
        TransformerMapper mapper = new TransformerMapper();
        mapper.assemblyTransformer = AssembledValueTransformer.create(FieldAssembler.create());
        mapper.disassemblyTransformer = DisassembledValueTransformer.create(FieldDisassembler.create());
        return mapper;
    }

}
