package xyz.phanta.psireagents.piececost;

import net.minecraft.util.ResourceLocation;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.piececost.impl.PieceCostProviderConstant;
import xyz.phanta.psireagents.piececost.impl.PieceCostProviderCostLinear;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class PieceCostProviderFactoryRegistry {

    private final Map<ResourceLocation, PieceCostProviderFactory> factories = new HashMap<>();

    public PieceCostProviderFactoryRegistry() {
        registerFactory(PieceCostProviderConstant.TYPE_ID, new PieceCostProviderConstant.Factory());
        registerFactory(PieceCostProviderCostLinear.TYPE_ID, new PieceCostProviderCostLinear.Factory());
    }

    public void registerFactory(ResourceLocation id, PieceCostProviderFactory factory) {
        PieceCostProviderFactory replaced = factories.put(id, factory);
        if (replaced != null) {
            PsiReagents.LOGGER.warn("Replacing existing cost provider factory for id {}!", id);
            PsiReagents.LOGGER.warn("{} -> {}", replaced, factory);
        }
    }

    public PieceCostProviderFactory getFactory(ResourceLocation id) {
        PieceCostProviderFactory factory = factories.get(id);
        if (factory == null) {
            throw new NoSuchElementException("Unknown cost provider factory type " + id + "!");
        }
        return factory;
    }

}
