package xyz.phanta.psireagents.piececost.impl;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellPiece;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.piececost.PieceCostProvider;
import xyz.phanta.psireagents.piececost.PieceCostProviderFactory;
import xyz.phanta.psireagents.reagent.ReagentQuantity;

import javax.annotation.Nullable;

public class PieceCostProviderCostLinear implements PieceCostProvider<SpellPiece> {

    public static final ResourceLocation TYPE_ID = PsiReagents.INSTANCE.newResourceLocation("cost_linear");

    private final ReagentQuantity baseCost;
    private final float factor;

    public PieceCostProviderCostLinear(ReagentQuantity baseCost, float factor) {
        this.baseCost = baseCost;
        this.factor = factor;
    }

    @Override
    public ReagentQuantity getCost(SpellPiece piece) {
        SpellMetadata meta = new SpellMetadata();
        try {
            piece.addToMetadata(meta);
        } catch (SpellCompilationException e) {
            return ReagentQuantity.NONE;
        }
        return baseCost.scale(meta.stats.get(EnumSpellStat.COST) * factor);
    }

    @Nullable
    @Override
    public JsonObject serializeJson() {
        JsonObject dto = baseCost.serializeJson();
        dto.addProperty("type", TYPE_ID.toString());
        dto.addProperty("factor", factor);
        return dto;
    }

    public static class Factory implements PieceCostProviderFactory {

        @Override
        public PieceCostProviderCostLinear createCostProvider(JsonObject dto) {
            return new PieceCostProviderCostLinear(ReagentQuantity.deserializeJson(dto), dto.get("factor").getAsFloat());
        }

    }

}
