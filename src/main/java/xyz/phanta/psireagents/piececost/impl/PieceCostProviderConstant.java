package xyz.phanta.psireagents.piececost.impl;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.spell.SpellPiece;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.piececost.PieceCostProvider;
import xyz.phanta.psireagents.piececost.PieceCostProviderFactory;
import xyz.phanta.psireagents.reagent.ReagentQuantity;

import javax.annotation.Nullable;

public class PieceCostProviderConstant implements PieceCostProvider<SpellPiece> {

    public static final ResourceLocation TYPE_ID = PsiReagents.INSTANCE.newResourceLocation("constant");

    private final ReagentQuantity cost;

    public PieceCostProviderConstant(ReagentQuantity cost) {
        this.cost = cost;
    }

    @Override
    public ReagentQuantity getCost(SpellPiece piece) {
        return cost;
    }

    @Nullable
    @Override
    public JsonObject serializeJson() {
        JsonObject dto = cost.serializeJson();
        dto.addProperty("type", TYPE_ID.toString());
        return dto;
    }

    public static class Factory implements PieceCostProviderFactory {

        @Override
        public PieceCostProviderConstant createCostProvider(JsonObject dto) {
            return new PieceCostProviderConstant(ReagentQuantity.deserializeJson(dto));
        }

    }

}
