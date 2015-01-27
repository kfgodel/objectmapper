package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.dgarcia.objectmapper.api.ensemble.ObjectAssembler;
import ar.com.dgarcia.objectmapper.api.ensemble.ObjectDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.FieldAssembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.FieldDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.AssembledValueTransformer;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.DisassembledValueTransformer;
import ar.com.kfgodel.diamond.api.Diamond;

import java.util.Map;

/**
 * This type represents an object mapper that uses different assembly/disassembly techniques
 * Created by kfgodel on 20/11/14.
 */
public class EnsembleMapper implements TypeMapper {

    private ObjectDisassembler disassembler;
    private ObjectAssembler assembler;

    @Override
    public Map<String, Object> toMap(Object instance) {
        return disassembler.disassemble(instance);
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        return assembler.assemble(map, Diamond.of(expectedType));
    }

    public ObjectDisassembler getDisassembler() {
        return disassembler;
    }

    public ObjectAssembler getAssembler() {
        return assembler;
    }

    public static EnsembleMapper create() {
        EnsembleMapper mapper = new EnsembleMapper();
        mapper.disassembler = FieldDisassembler.create(DisassembledValueTransformer.create());
        mapper.assembler = FieldAssembler.create(AssembledValueTransformer.create());
        return mapper;
    }

}
