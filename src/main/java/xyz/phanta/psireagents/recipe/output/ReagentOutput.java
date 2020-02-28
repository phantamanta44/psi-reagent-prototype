package xyz.phanta.psireagents.recipe.output;

import io.github.phantamanta44.libnine.recipe.output.IRcpOut;
import xyz.phanta.psireagents.reagent.ReagentQuantity;

public class ReagentOutput implements IRcpOut<ReagentQuantity> {

    private final ReagentQuantity reagents;

    public ReagentOutput(ReagentQuantity reagents) {
        this.reagents = reagents;
    }

    public ReagentQuantity getReagents() {
        return reagents;
    }

    @Override
    public boolean isAcceptable(ReagentQuantity env) {
        return true;
    }

}
