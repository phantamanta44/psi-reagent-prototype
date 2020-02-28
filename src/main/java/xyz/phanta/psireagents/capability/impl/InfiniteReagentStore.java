package xyz.phanta.psireagents.capability.impl;

import xyz.phanta.psireagents.capability.ReagentStore;
import xyz.phanta.psireagents.reagent.Reagent;

public class InfiniteReagentStore implements ReagentStore {

    @Override
    public int getReagentQuantity(Reagent reagent) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getReagentCapacity(Reagent reagent) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int extractReagent(Reagent reagent, int maxQty, boolean commit) {
        return maxQty;
    }

    @Override
    public int injectReagent(Reagent reagent, int maxQty, boolean commit) {
        return 0;
    }

    @Override
    public float getCostMultiplier() {
        return 0F;
    }

}
