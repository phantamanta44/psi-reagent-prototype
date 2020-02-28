package xyz.phanta.psireagents.capability;

import xyz.phanta.psireagents.reagent.Reagent;
import xyz.phanta.psireagents.reagent.ReagentQuantity;

public interface ReagentStore {

    ReagentStore EMPTY = new Noop();

    int getReagentQuantity(Reagent reagent);

    int getReagentCapacity(Reagent reagent);

    default int getRemainingReagentCapacity(Reagent reagent) {
        return getReagentCapacity(reagent) - getReagentQuantity(reagent);
    }

    default boolean containsReagent(Reagent reagent, int cost) {
        return getReagentQuantity(reagent) >= cost;
    }

    int extractReagent(Reagent reagent, int maxQty, boolean commit);

    int injectReagent(Reagent reagent, int maxQty, boolean commit);

    default void transferTo(ReagentStore other) {
        for (Reagent reagent : Reagent.VALUES) {
            int qty = other.getRemainingReagentCapacity(reagent);
            if (qty > 0) {
                qty = extractReagent(reagent, qty, true);
                if (qty > 0) {
                    qty = other.injectReagent(reagent, qty, true);
                    if (qty > 0) {
                        injectReagent(reagent, qty, true);
                    }
                }
            }
        }
    }

    default ReagentQuantity computeQuantity() {
        ReagentQuantity.Builder builder = new ReagentQuantity.Builder();
        for (Reagent reagent : Reagent.VALUES) {
            builder.add(reagent, getReagentQuantity(reagent));
        }
        return builder.build();
    }

    default float getCostMultiplier() {
        return 1F;
    }

    class Noop implements ReagentStore {

        @Override
        public int getReagentQuantity(Reagent reagent) {
            return 0;
        }

        @Override
        public int getReagentCapacity(Reagent reagent) {
            return 0;
        }

        @Override
        public int extractReagent(Reagent reagent, int maxQty, boolean commit) {
            return 0;
        }

        @Override
        public int injectReagent(Reagent reagent, int maxQty, boolean commit) {
            return 0;
        }

    }

}
