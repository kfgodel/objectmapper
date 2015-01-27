package ar.com.dgarcia.objectmapper.api.ensemble;

import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.Map;

/**
 * This type represents an object assembler that sets the state of an object from a map
 * Created by kfgodel on 20/11/14.
 */
public interface ObjectAssembler {

    /**
     * Assembles the primitive parts contained in the given map into an object of the expected type.<br>
     *     This can be called recursively if the instance has other instance to build
     * @param map The map containing the separate parts
     * @param expectedType The expected instance type
     * @param <T> The type of instance
     * @return The created instance with the state from the map
     */
    <T> T assemble(Map<String,Object> map, TypeInstance expectedType);

    void setAssemblyTransformer(AssemblyTransformer transformer);
}
