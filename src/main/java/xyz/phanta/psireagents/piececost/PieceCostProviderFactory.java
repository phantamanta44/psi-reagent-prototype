package xyz.phanta.psireagents.piececost;

import com.google.gson.JsonObject;
import vazkii.psi.api.spell.SpellPiece;

@FunctionalInterface
public interface PieceCostProviderFactory {

    PieceCostProvider<SpellPiece> createCostProvider(JsonObject dto);

}
