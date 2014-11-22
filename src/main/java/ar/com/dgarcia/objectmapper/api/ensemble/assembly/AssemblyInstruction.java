package ar.com.dgarcia.objectmapper.api.ensemble.assembly;

/**
 * This type represents an instruction to add a part to an object as part of its assembly
 * Created by kfgodel on 21/11/14.
 */
public interface AssemblyInstruction {
    /**
     * @return The name that identifies the assembled part
     */
    String getPartName();

    /**
     * Sets the part into the object assembling it
     * @param instance The object to assemble the part
     * @param part The part to add in the object
     */
    void setPartOn(Object instance, Object part);
}
