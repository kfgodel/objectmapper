package ar.com.dgarcia.objectmapper.api.ensemble.disassembly;

/**
 * This type represents the disassembly instructions for a given type
 * Created by kfgodel on 20/11/14.
 */
public interface DisassemblyPlan {
    /**
     * @return Returns the ordered instructions to disassemble an object according to this plan
     */
    DisassemblyInstruction[] getInstructions();
}
