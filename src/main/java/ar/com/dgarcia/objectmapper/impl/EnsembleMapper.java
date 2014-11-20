package ar.com.dgarcia.objectmapper.impl;

import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.dgarcia.objectmapper.api.ensemble.ObjectDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.FieldDisassembler;

import java.util.Map;

/**
 * This type represents an object mapper that uses different assembly/disassembly techniques
 * Created by kfgodel on 20/11/14.
 */
public class EnsembleMapper implements TypeMapper {

    private ObjectDisassembler disassembler;

    @Override
    public Map<String, Object> toMap(Object instance) {
        return disassembler.disassemble(instance);
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        return null;
    }

    public static EnsembleMapper create() {
        EnsembleMapper mapper = new EnsembleMapper();
        mapper.disassembler = FieldDisassembler.create();
        return mapper;
    }

}
