package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly;

import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyInstruction;
import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyPlan;

import java.util.List;

/**
 * This type represents a disassembly plan composed of an array of instructions
 * Created by kfgodel on 20/11/14.
 */
public class DisassemblyArrayPlan implements DisassemblyPlan {

    private DisassemblyInstruction[] instructions;

    public static DisassemblyArrayPlan create(List<DisassemblyInstruction> instructionList) {
        DisassemblyArrayPlan description = new DisassemblyArrayPlan();
        description.instructions = instructionList.toArray(new DisassemblyInstruction[instructionList.size()]);
        return description;
    }

    @Override
    public DisassemblyInstruction[] getInstructions() {
        return instructions;
    }
}
