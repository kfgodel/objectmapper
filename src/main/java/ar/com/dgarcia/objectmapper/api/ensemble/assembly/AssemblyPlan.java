package ar.com.dgarcia.objectmapper.api.ensemble.assembly;

import java.util.function.Supplier;

/**
 * This type represents an assembly plan to create an new instance from its parts
 * Created by kfgodel on 21/11/14.
 */
public interface AssemblyPlan {
    /**
     * @return The instructions to assemble instances according to this plan
     */
    AssemblyInstruction[] getInstructions();

    /**
     * The supplier of new instances for this plan
     */
    <T> Supplier<T> getInstanceSupplier();
}
