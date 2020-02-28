package xyz.phanta.psireagents.piececost;

import com.google.gson.JsonObject;
import vazkii.psi.api.spell.SpellPiece;
import xyz.phanta.psireagents.reagent.ReagentQuantity;

import javax.annotation.Nullable;

@FunctionalInterface
public interface PieceCostProvider<P extends SpellPiece> {

    ReagentQuantity getCost(P piece);

    @Nullable
    default JsonObject serializeJson() {
        return null;
    }

}
