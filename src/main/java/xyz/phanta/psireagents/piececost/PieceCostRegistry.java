package xyz.phanta.psireagents.piececost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.phantamanta44.libnine.util.helper.JsonUtils9;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellGrid;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.trick.PieceTrickBlaze;
import vazkii.psi.common.spell.trick.PieceTrickSmite;
import vazkii.psi.common.spell.trick.entity.PieceTrickBlink;
import vazkii.psi.common.spell.trick.infusion.PieceTrickGreaterInfusion;
import vazkii.psi.common.spell.trick.infusion.PieceTrickInfusion;
import xyz.phanta.psireagents.PsiReagents;
import xyz.phanta.psireagents.piececost.impl.PieceCostProviderConstant;
import xyz.phanta.psireagents.piececost.impl.PieceCostProviderCostLinear;
import xyz.phanta.psireagents.reagent.Reagent;
import xyz.phanta.psireagents.reagent.ReagentQuantity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PieceCostRegistry {

    private static final String COST_CONFIG_FILE_NAME = "psi_reagents_costs.json";
    private static final Gson PRETTY_PRINTER = new GsonBuilder().setPrettyPrinting().create();

    private final Path costConfigFile;
    private final Map<String, PieceCostProvider<?>> costProviderRegistry = new HashMap<>();

    public PieceCostRegistry(Path configDir) {
        this.costConfigFile = configDir.resolve(COST_CONFIG_FILE_NAME);
    }

    private void loadDefaults() {
        costProviderRegistry.clear();
        // TODO default costs
        registerCostProvider(PieceTrickSmite.class, new PieceCostProviderConstant(
                new ReagentQuantity.Builder().add(Reagent.ELEM_FIRE, 400).add(Reagent.ARCH_CONJURE, 400).build()));
        // registerCostProvider(PieceTrickAddMotion.class, null);
         registerCostProvider(PieceTrickBlaze.class, new PieceCostProviderConstant(
                 new ReagentQuantity.Builder().add(Reagent.ELEM_FIRE, 100).build()));
        registerCostProvider(PieceTrickBlink.class, new PieceCostProviderCostLinear(
                new ReagentQuantity.Builder().add(Reagent.ARCH_MODIFY, 1).build(), 2.5F));
        // registerCostProvider(PieceTrickBreakBlock.class, null);
        // registerCostProvider(PieceTrickBreakInSequence.class, null);
        // registerCostProvider(PieceTrickCollapseBlock.class, null);
        // registerCostProvider(PieceTrickConjureBlock.class, null);
        // registerCostProvider(PieceTrickConjureBlockSequence.class, null);
        // registerCostProvider(PieceTrickConjureLight.class, null);
        // registerCostProvider(PieceTrickDebug.class, null);
        // registerCostProvider(PieceTrickDelay.class, null);
        // registerCostProvider(PieceTrickDie.class, null);
        // registerCostProvider(PieceTrickEbonyIvory.class, null);
        // registerCostProvider(PieceTrickEidosAnchor.class, null);
        // registerCostProvider(PieceTrickEidosReversal.class, null);
        // registerCostProvider(PieceTrickEvaluate.class, null);
        // registerCostProvider(PieceTrickExplode.class, null);
        // registerCostProvider(PieceTrickFireResistance.class, null);
         registerCostProvider(PieceTrickGreaterInfusion.class, new PieceCostProviderConstant(
                 new ReagentQuantity.Builder().add(Reagent.ELEM_FIRE, 500).add(Reagent.ELEM_WATER, 500)
                         .add(Reagent.ELEM_EARTH, 500).add(Reagent.ELEM_AIR, 500).add(Reagent.ARCH_MODIFY, 6000).build()));
        // registerCostProvider(PieceTrickHaste.class, null);
        // registerCostProvider(PieceTrickIgnite.class, null);
        registerCostProvider(PieceTrickInfusion.class, new PieceCostProviderConstant(
                new ReagentQuantity.Builder().add(Reagent.ELEM_FIRE, 250).add(Reagent.ELEM_WATER, 250)
                        .add(Reagent.ELEM_EARTH, 250).add(Reagent.ELEM_AIR, 250).add(Reagent.ARCH_MODIFY, 1000).build()));
        // registerCostProvider(PieceTrickInvisibility.class, null);
        // registerCostProvider(PieceTrickJumpBoost.class, null);
        // registerCostProvider(PieceTrickMassAddMotion.class, null);
        // registerCostProvider(PieceTrickMassBlink.class, null);
        // registerCostProvider(PieceTrickMassExodus.class, null);
        // registerCostProvider(PieceTrickMoveBlock.class, null);
        // registerCostProvider(PieceTrickOvergrow.class, null);
        // registerCostProvider(PieceTrickPlaceBlock.class, null);
        // registerCostProvider(PieceTrickPlaceInSequence.class, null);
        // registerCostProvider(PieceTrickPotionBase.class, null);
        // registerCostProvider(PieceTrickRegeneration.class, null);
        // registerCostProvider(PieceTrickResistance.class, null);
        // registerCostProvider(PieceTrickSaveVector.class, null);
        // registerCostProvider(PieceTrickSlowness.class, null);
        // registerCostProvider(PieceTrickSmeltBlock.class, null);
        // registerCostProvider(PieceTrickSmeltItem.class, null);
        // registerCostProvider(PieceTrickSmite.class, null);
        // registerCostProvider(PieceTrickSpeed.class, null);
        // registerCostProvider(PieceTrickStrength.class, null);
        // registerCostProvider(PieceTrickSwitchTargetSlot.class, null);
        // registerCostProvider(PieceTrickTorrent.class, null);
        // registerCostProvider(PieceTrickWaterBreathing.class, null);
        // registerCostProvider(PieceTrickWeakness.class, null);
        // registerCostProvider(PieceTrickWither.class, null);
    }

    public <P extends SpellPiece> void registerCostProvider(Class<P> type, PieceCostProvider<? super P> costProvider) {
        String pieceId = PsiAPI.spellPieceRegistry.getNameForObject(type);
        if (pieceId != null) {
            registerCostProviderUnchecked(pieceId, costProvider);
        } else {
            PsiReagents.LOGGER.warn("Skipping spell piece type {} with null registry name!", type.getName());
        }
    }

    private <P extends SpellPiece> void registerCostProviderUnchecked(String pieceId, PieceCostProvider<P> costProvider) {
        PieceCostProvider<?> replaced = costProviderRegistry.put(pieceId, costProvider);
        if (replaced != null) {
            PsiReagents.LOGGER.warn("Replacing existing cost provider for spell piece {}!", pieceId);
            PsiReagents.LOGGER.warn("{} -> {}", replaced, costProvider);
        }
    }

    public void removeCostProvider(Class<? extends SpellPiece> type) {
        String pieceId = PsiAPI.spellPieceRegistry.getNameForObject(type);
        if (pieceId != null) {
            removeCostProvider(pieceId);
        } else {
            PsiReagents.LOGGER.warn("Could not remove spell cost for piece type {} with null registry name!", type.getName());
        }
    }

    public void removeCostProvider(String pieceId) {
        costProviderRegistry.remove(pieceId);
    }

    public ReagentQuantity getCost(SpellPiece piece) {
        PieceCostProvider<?> costProvider = costProviderRegistry.get(piece.registryKey);
        return costProvider != null ? getCostUnchecked(costProvider, piece) : ReagentQuantity.NONE;
    }

    private static <P extends SpellPiece> ReagentQuantity getCostUnchecked(PieceCostProvider<P> costProvider, SpellPiece piece) {
        //noinspection unchecked
        return costProvider.getCost((P)piece);
    }

    public ReagentQuantity computeTotalCost(SpellGrid grid) {
        ReagentQuantity.Builder builder = new ReagentQuantity.Builder();
        for (SpellPiece[] row : grid.gridData) {
            for (SpellPiece piece : row) {
                if (piece != null) {
                    builder.add(getCost(piece));
                }
            }
        }
        return builder.build();
    }

    public void reloadFromDisk(boolean loadDefaults) {
        if (Files.exists(costConfigFile)) {
            PsiReagents.LOGGER.info("Loading piece cost config...");
            PieceCostProviderFactoryRegistry factoryRegistry = PsiReagents.PROXY.getPieceCostProviders();
            JsonObject dto;
            try (BufferedReader in = Files.newBufferedReader(costConfigFile)) {
                dto = JsonUtils9.PARSER.parse(in).getAsJsonObject();
            } catch (Exception e) {
                PsiReagents.LOGGER.error("Failed to load piece cost config! Defaults loaded as fallback.", e);
                loadDefaults();
                return;
            }
            for (Map.Entry<String, JsonElement> entry : dto.entrySet()) {
                try {
                    JsonObject providerDto = entry.getValue().getAsJsonObject();
                    registerCostProviderUnchecked(entry.getKey(), factoryRegistry
                            .getFactory(new ResourceLocation(providerDto.get("type").getAsString()))
                            .createCostProvider(providerDto));
                } catch (Exception e) {
                    PsiReagents.LOGGER.warn("Could not parse cost config for piece " + entry.getKey() + "!", e);
                }
            }
        } else if (loadDefaults) {
            PsiReagents.LOGGER.info("No piece cost config found; defaults loaded.");
            loadDefaults();
        } else {
            PsiReagents.LOGGER.info("No piece cost config found.");
        }
    }

    public void saveToDisk() {
        PsiReagents.LOGGER.info("Writing piece cost config to disk...");
        JsonObject dto = new JsonObject();
        costProviderRegistry.forEach((pieceId, costProvider) -> {
            JsonObject providerDto = costProvider.serializeJson();
            if (providerDto != null) {
                dto.add(pieceId, providerDto);
            }
        });
        try (BufferedWriter out = Files.newBufferedWriter(costConfigFile)) {
            out.write(PRETTY_PRINTER.toJson(dto));
        } catch (Exception e) {
            PsiReagents.LOGGER.error("Failed to write piece cost config!", e);
        }
    }

}
