package ar.com.dgarcia.objectmapper.api.ensemble;

import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;

import java.util.Map;
import java.util.function.Function;

/**
 * This type represents an object disassembler that maps the state of an object into a map
 * Created by kfgodel on 20/11/14.
 */
public interface ObjectDisassembler extends Function<Object, Object> {

    /**
     * Creates a map with the state of the state of the object consisting only of primitive types (numbers and strings).<br>
     *     This process may recursively be used for objects referenced by the given instance
     * @param instance The instance to represent
     * @return The map of values
     */
    Map<String,Object> disassemble(Object instance);

    /**
     * Used as a function this instance disassembles the object
     * @param instance The object to disassemble
     * @return The disassebled version of the object
     */
    default Object apply(Object instance){
        return disassemble(instance);
    }

    /**
     * The internal transformer used for type conversion
     * @return The transformer used for type conversions
     */
    DisassemblyTransformer getDisassemblyTransformer();

    void setDisassemblyTransformer(DisassemblyTransformer transformer);
}
