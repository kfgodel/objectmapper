package ar.com.kfgodel.objectmapper.tests.mapper;

import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.kfgodel.objectmapper.tests.testObjects.TypicalObject;

import java.util.Map;

/**
 * This type represents a custom implementation of the type mapper only for TypicalObject
 * Created by kfgodel on 18/11/14.
 */
public class TypicalObjectMapper implements TypeMapper {

    private CustomMadeTypicalObjectAssembler assembler;
    private CustomMadeTypicalObjectDisassembler disassembler;

    @Override
    public Map<String, Object> toMap(Object instance) {
        return disassembler.apply((TypicalObject)instance);
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> expectedType) {
        return (T) assembler.apply(map);
    }

    public static TypicalObjectMapper create() {
        TypicalObjectMapper mapper = new TypicalObjectMapper();
        mapper.assembler = CustomMadeTypicalObjectAssembler.create();
        mapper.disassembler = CustomMadeTypicalObjectDisassembler.create();
        return mapper;
    }

}
